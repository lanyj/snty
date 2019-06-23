package cn.lanyj.snty.server;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lanyj.snty.common.codec.DefaultMessageToMessageCodec;
import cn.lanyj.snty.common.config.GlobalConfig;
import cn.lanyj.snty.common.lifecycle.NettyLifeCycleManager;
import cn.lanyj.snty.server.execptionhandler.ExceptionChannelHandler;
import cn.lanyj.snty.server.sessionhandler.SessionChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public abstract class NettyServer {

	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	protected final GlobalConfig config = GlobalConfig.getInstance();

	NettyLifeCycleManager nettyLifeCycleManager = new NettyLifeCycleManager();

	EventLoopGroup bossGroup;
	EventLoopGroup workGroup;
	ServerBootstrap bootstrap;
	Channel channel;

	public NettyServer() {
		init();
	}

	public void init() {
		this.bossGroup = new NioEventLoopGroup();
		this.workGroup = new NioEventLoopGroup();
		this.bootstrap = new ServerBootstrap();

		this.bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class);
		registerChannelHandlers();
	}

	private void registerChannelHandlers() {
		this.bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("LengthFieldBasedFrameDecoder",
						new LengthFieldBasedFrameDecoder(config.frameBasedConfig.frameBasedMaxFrameLength(),
								config.frameBasedConfig.frameBasedLengthFieldOffset(),
								config.frameBasedConfig.frameBasedLengthFieldLength(),
								config.frameBasedConfig.frameBasedLengthAdjustment(),
								config.frameBasedConfig.frameBasedInitialBytesToStrip()));
				pipeline.addLast("LengthFieldPrepender",
						new LengthFieldPrepender(config.frameBasedConfig.frameBasedLengthFieldLength()));
				pipeline.addLast("CommandCodec", new DefaultMessageToMessageCodec());
				if (config.loggingConfig.channelPipeLineUsingLoggingHandler()) {
					pipeline.addLast("Logger", new LoggingHandler(config.loggingConfig.channelPipeLineLoggingLevel()));
					logger.info("Using log handler, level = {}",
							config.loggingConfig.channelPipeLineLoggingLevel().name());
				}
				if (config.sessionConfig.usingSession()) {
					pipeline.addLast("SessionChannelHandler", SessionChannelHandler.getInstance());
				}
				addChannelHandlers(pipeline);
				pipeline.addLast("ExceptionChannelHandler", new ExceptionChannelHandler());
			}
		});
	}

	public void bind(InetAddress address, int port) {
		ChannelFuture future;
		try {
			future = this.bootstrap.bind(address, port).sync();
			channel = future.channel();
			future.addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isDone() && future.isSuccess()) {
						logger.info("Server startup successfully, listening on: {}:{}", address, port);
						nettyLifeCycleManager.startUpSuccess();
					} else {
						logger.error("Server startup failed", future.cause());
						nettyLifeCycleManager.startUpFailed(future.cause());
					}
				}
			});
		} catch (InterruptedException e) {
			logger.error("Server bind failed", e);
		}
	}

	public void stop() {
		if (this.getChannel() != null) {
			try {
				ChannelFuture future = getChannel().close().sync();
				future.addListener(new ChannelFutureListener() {

					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						if (future.isDone() && future.isSuccess()) {
							logger.info("Server shutdown successfully");
							nettyLifeCycleManager.shutdownSuccess();
							channel = null;
						} else {
							logger.error("Server shutdown failed", future.cause());
							nettyLifeCycleManager.shutdownFailed(future.cause());
						}
					}
				});
			} catch (InterruptedException e) {
				logger.error("Server shutdown interrupted exception", e);
			}
		} else {
			logger.warn("Server channel is null, is start already?");
		}
	}

	public Channel getChannel() {
		return this.channel;
	}

	public abstract void addChannelHandlers(ChannelPipeline pipeline);

	public NettyLifeCycleManager getNettyLifeCycleManager() {
		return this.nettyLifeCycleManager;
	}

	public void destory() {
		if (getChannel() != null) {
			stop();
		}
		workGroup.shutdownGracefully().addListener(new FutureListener<Object>() {
			@Override
			public void operationComplete(Future<Object> future) throws Exception {
				logger.info("Server workEventGroup destory successfully");
			}
		});
		bossGroup.shutdownGracefully().addListener(new FutureListener<Object>() {
			@Override
			public void operationComplete(Future<Object> future) throws Exception {
				logger.info("Server bossEventGroup destory successfully");
			}
		});
	}
}
