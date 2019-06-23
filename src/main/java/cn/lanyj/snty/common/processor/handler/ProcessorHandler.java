package cn.lanyj.snty.common.processor.handler;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.ProcessorContext;
import cn.lanyj.snty.common.processor.ProcessorManager;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.impl.DefaultProcessorContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ProcessorHandler implements ChannelInboundHandler {

	private static final AttributeKey<ProcessorContext> CONTEXT = AttributeKey.valueOf("processor_context");

	ProcessorManager manager;

	public ProcessorHandler(ProcessorManager processorManager) {
		this.manager = processorManager;
	}

	private ProcessorContext getOrCreateProcessorContext(ChannelHandlerContext context) {
		Attribute<ProcessorContext> attribute = context.channel().attr(CONTEXT);
		ProcessorContext processorContext = attribute.get();
		if (processorContext == null) {
			processorContext = new DefaultProcessorContext(context, manager);
			attribute.set(processorContext);
		}
		return processorContext;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx).handleChannelStateChanged(State.ADDED);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx).handleChannelStateChanged(State.REMOVED);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx).handleChannelStateChanged(State.REGISTERED);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx).handleChannelStateChanged(State.UNREGISTERED);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx).handleChannelStateChanged(State.ACTIVE);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx).handleChannelStateChanged(State.INACTIVE);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Message) {
			getOrCreateProcessorContext(ctx).handleMessage((Message<?>) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		getOrCreateProcessorContext(ctx).handleEvent(evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx).handleChannelStateChanged(State.WRITABILITY_CHANGED);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		getOrCreateProcessorContext(ctx).handleException(cause);
	}

}
