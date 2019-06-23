package cn.lanyj.snty;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cn.lanyj.snty.client.NettyClient;
import cn.lanyj.snty.common.config.GlobalConfig;
import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.utils.SessionUtils;
import cn.lanyj.snty.common.utils.UncaughtExceptionUtil;
import cn.lanyj.snty.helper.IntMessage;
import cn.lanyj.snty.server.NettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

public class TestServerAndClient {

	public static void main(String[] args) throws Exception {
		UncaughtExceptionUtil.declare();

		GlobalConfig config = GlobalConfig.getInstance();
		config.sessionConfig.setUsingSession(true);
		config.loggingConfig.setChannelPipeLineUsingLoggingHandler(false);

		new Thread(new Runnable() {

			@Override
			public void run() {
				NettyServer server = new NettyServer() {

					@Override
					public void addChannelHandlers(ChannelPipeline pipeline) {
						pipeline.addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								super.channelRead(ctx, msg);

								Session session = SessionUtils.getSessionFromChannel(ctx.channel());
								System.out.println(session.getOrDefault("content", -1));

								IntMessage message = (IntMessage) msg;
								message.setContent(message.content + 1);
								ctx.writeAndFlush(message);

								session.set("content", message.content);
							}

						});
					}
				};
				try {
					server.bind(InetAddress.getByName("localhost"), 8080);

					Thread.sleep(20000);
					server.destory();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Thread.sleep(3000);

		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					NettyClient client = new NettyClient() {

						@Override
						public void addChannelHandlers(ChannelPipeline pipeline) {
							pipeline.addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									super.channelRead(ctx, msg);

									IntMessage message = (IntMessage) msg;
									message.setContent(message.content + 1);
									ctx.writeAndFlush(message);
								}
							});
						}
					};
					try {
						client.connect(InetAddress.getByName("localhost"), 8080);

						Thread.sleep(1000);
						IntMessage message = new IntMessage();
						ChannelFuture future = client.getChannel().writeAndFlush(message);
						future.addListener(new ChannelFutureListener() {

							@Override
							public void operationComplete(ChannelFuture future) throws Exception {
								System.out.println(future);
							}
						});

						Thread.sleep(3000);
						client.destory();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
