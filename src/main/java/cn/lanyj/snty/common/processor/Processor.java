package cn.lanyj.snty.common.processor;

import java.io.Serializable;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.exception.ProcessException;

public interface Processor {

	/**
	 * 
	 * @param context
	 * @param msg
	 * @return <code>true></code> for process not finished
	 * @throws ProcessException
	 */
	default <T extends Serializable> boolean handleMessage(ProcessorContext context, Message<T> msg)
			throws ProcessException {
		return false;
	}

	default boolean handleEvent(ProcessorContext context, Object event) throws ProcessException {
		return false;
	}

	default boolean handleException(ProcessorContext context, Throwable t) throws ProcessException {
		return false;
	}

	default boolean handleChannelStateChanged(ProcessorContext context, State pre, State cur) throws ProcessException {
		return false;
	}

}
