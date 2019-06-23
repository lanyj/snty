package cn.lanyj.snty.server.execptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class ExceptionChannelHandler implements ChannelHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionChannelHandler.class);

	private static final ExceptionChannelHandler instance = new ExceptionChannelHandler();

	public static ExceptionChannelHandler getInstance() {
		return instance;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Uncaught error", cause);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
	}
}
