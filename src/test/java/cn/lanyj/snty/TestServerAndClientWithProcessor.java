package cn.lanyj.snty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import cn.lanyj.snty.client.NettyClient;
import cn.lanyj.snty.common.config.GlobalConfig;
import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.Processor;
import cn.lanyj.snty.common.processor.ProcessorContext;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.ProcessorManager;
import cn.lanyj.snty.common.processor.annotation.AnnotationBasedProcessorFactory;
import cn.lanyj.snty.common.processor.annotation.ProcessorMethod;
import cn.lanyj.snty.common.processor.exception.ProcessException;
import cn.lanyj.snty.common.processor.handler.ProcessorHandler;
import cn.lanyj.snty.common.processor.impl.DefaultProcessorManager;
import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.utils.SessionUtils;
import cn.lanyj.snty.common.utils.UncaughtExceptionUtil;
import cn.lanyj.snty.helper.DoubleMessage;
import cn.lanyj.snty.helper.IntMessage;
import cn.lanyj.snty.helper.StrMessage;
import cn.lanyj.snty.server.NettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

public class TestServerAndClientWithProcessor {

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

								if (msg instanceof IntMessage) {
									Session session = SessionUtils.getSessionFromChannel(ctx.channel());

									IntMessage message = (IntMessage) msg;
									message.setContent(message.content + 1);
									ctx.writeAndFlush(message);

									if (Math.random() < 0.2f) {
										StrMessage s = new StrMessage();
										s.setContent(UUID.randomUUID().toString());
										ctx.writeAndFlush(s);
									}

									if (Math.random() < 0.2f) {
										DoubleMessage s = new DoubleMessage();
										s.setContent(Math.random() * 1000);
										ctx.writeAndFlush(s);
									}

									session.set("content", message.content);
								}
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

		for (int i = 0; i < 15; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					NettyClient client = new NettyClient() {

						@Override
						public void addChannelHandlers(ChannelPipeline pipeline) {
							pipeline.addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									if (msg instanceof IntMessage) {
										IntMessage message = (IntMessage) msg;
										message.setContent(message.content + 1);
										ctx.writeAndFlush(message);
									}

									super.channelRead(ctx, msg);
								}
							});
							ProcessorManager manager = new DefaultProcessorManager();
							Processor processor = AnnotationBasedProcessorFactory.newProcessor(PrintProcessor.class,
									new PrintProcessor());
							manager.addLast("test", processor);
							pipeline.addLast("processorHandler", new ProcessorHandler(manager));
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
//								System.out.println(future);
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

	public static class PrintProcessor {
		@ProcessorMethod
		public boolean onMessage(ProcessorContext context, Message<?> message) {
			System.out.println(context + " On messg3: " + message);
			return true;
		}

		@ProcessorMethod
		public boolean onMessage(ProcessorContext context, StrMessage message) {
			System.out.println(context + " On messg2: " + message);
			return true;
		}

		@ProcessorMethod
		public boolean onMessage(ProcessorContext context, IntMessage message) {
			System.out.println(context + " On messg1: " + message);
			return true;
		}

		@ProcessorMethod
		public boolean onEvent(ProcessorContext context, Object event) {
			System.out.println(context + " On event: " + event);
			return true;
		}

		@ProcessorMethod
		public boolean onException(ProcessorContext context, Throwable t) throws ProcessException {
			System.out.println(context + " On excpt: " + t);
			return true;
		}

		@ProcessorMethod
		public boolean handleChannelStateChanged(ProcessorContext context, State pre, State cur)
				throws ProcessException {
			System.out.println(context + " On state: " + pre + " ===>>> " + cur);
			return true;
		}
	}
}
