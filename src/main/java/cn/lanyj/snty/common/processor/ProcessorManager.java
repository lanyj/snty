package cn.lanyj.snty.common.processor;

import java.io.Serializable;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.exception.DuplicateProcessorException;
import cn.lanyj.snty.common.processor.exception.NotFoundProcessorException;
import cn.lanyj.snty.common.processor.exception.ProcessException;

/**
 * holds processors
 * 
 * @see Processor
 * @author lanyj
 *
 */
public interface ProcessorManager {

	ProcessorManager addLast(String name, Processor processor) throws DuplicateProcessorException;

	ProcessorManager addBefore(String beforeName, String name, Processor processor) throws NotFoundProcessorException;

	ProcessorManager replace(String beforeName, String nowName, Processor processor)
			throws NotFoundProcessorException, DuplicateProcessorException;

	ProcessorManager remove(String name) throws NotFoundProcessorException;

	void setDefaultProcessor(Processor processor);

	boolean handleEvent(ProcessorContext context, Object event) throws ProcessException;

	boolean handleException(ProcessorContext context, Throwable t) throws ProcessException;

	boolean handleChannelStateChanged(ProcessorContext context, State pre, State cur) throws ProcessException;

	<T extends Serializable> boolean handleMessage(ProcessorContext context, Message<T> msg) throws ProcessException;

}
