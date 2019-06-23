package cn.lanyj.snty.common.processor;

import java.io.Serializable;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.exception.DuplicateProcessorException;
import cn.lanyj.snty.common.processor.exception.NotFoundProcessorException;
import cn.lanyj.snty.common.processor.exception.ProcessException;

public interface ProcessorManager {

	ProcessorManager addLast(String name, Processor processor) throws DuplicateProcessorException;

	ProcessorManager addBefore(String beforeName, String name, Processor processor)
			throws NotFoundProcessorException;

	ProcessorManager replace(String beforeName, String nowName, Processor processor)
			throws NotFoundProcessorException, DuplicateProcessorException;

	ProcessorManager remove(String name) throws NotFoundProcessorException;

	void setDefaultProcessor(Processor processor);

	void handleEvent(ProcessorContext context, Object event) throws ProcessException;

	void handleException(ProcessorContext context, Throwable t) throws ProcessException;

	void handleChannelStateChanged(ProcessorContext context, State pre, State cur) throws ProcessException;

	<T extends Serializable> void handleMessage(ProcessorContext context, Message<T> msg) throws ProcessException;

}
