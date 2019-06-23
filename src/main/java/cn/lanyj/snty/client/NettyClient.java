package cn.lanyj.snty.client;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lanyj.snty.common.codec.DefaultMessageToMessageCodec;
import cn.lanyj.snty.common.config.GlobalConfig;
import cn.lanyj.snty.common.lifecycle.NettyLifeCycleManager;
import cn.lanyj.snty.server.sessionhandler.SessionChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public abstract class NettyClient {

	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

	protected GlobalConfig config = GlobalConfig.getInstance();

	NettyLifeCycleManager nettyLifeCycleManager = new NettyLifeCycleManager();

	EventLoopGroup group;
	Bootstrap bootstrap;
	Channel channel;

	public NettyClient() {
		init();
	}

	public void init() {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup();

		this.bootstrap.group(group).channel(NioSocketChannel.class);
		this.registerChannelHandlers();
	}

	public void registerChannelHandlers() {
		this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
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
			}
		});
	}

	public void connect(InetAddress address, int port) {
		try {
			ChannelFuture future = this.bootstrap.connect(address, port).sync();
			channel = future.channel();
			future.addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isDone() && future.isSuccess()) {
						logger.info("Client connect successfully, {}:{}", address, port);
						nettyLifeCycleManager.startUpSuccess();
					} else {
						logger.error("Client connect failed", future.cause());
						nettyLifeCycleManager.startUpFailed(future.cause());
					}
				}
			});
		} catch (InterruptedException e) {
			logger.error("Client connect interrupted exception", e);
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
							logger.info("Client shutdown successfully");
							nettyLifeCycleManager.shutdownSuccess();
							channel = null;
						} else {
							logger.error("Client shutdown failed", future.cause());
							nettyLifeCycleManager.shutdownFailed(future.cause());
						}
					}
				});
			} catch (InterruptedException e) {
				logger.error("Client shutdown interrupted exception", e);
			}
		} else {
			logger.warn("Client channel is null, is start already?");
		}
	}

	public abstract void addChannelHandlers(ChannelPipeline pipeline);

	public Channel getChannel() {
		return this.channel;
	}

	public NettyLifeCycleManager getNettyLifeCycleManager() {
		return this.nettyLifeCycleManager;
	}

	public void destory() {
		if (getChannel() != null) {
			stop();
		}
		group.shutdownGracefully().addListener(new FutureListener<Object>() {
			@Override
			public void operationComplete(Future<Object> future) throws Exception {
				logger.info("Client eventGroup destory successfully");
			}
		});
	}

}
