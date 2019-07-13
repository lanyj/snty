package cn.lanyj.snty.common.processor.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.Processor;
import cn.lanyj.snty.common.processor.ProcessorContext;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.ProcessorManager;
import cn.lanyj.snty.common.processor.exception.DuplicateProcessorException;
import cn.lanyj.snty.common.processor.exception.NotFoundProcessorException;
import cn.lanyj.snty.common.processor.exception.ProcessException;

public class DefaultProcessorManager implements ProcessorManager {
	private static final Processor DEFAULT_PROCESSOR = new DefaultMessageProcessor();

	int index = 0;
	ProcessorWarper warpers[] = new ProcessorWarper[16];
	ProcessorWarper defaultProcessor = new ProcessorWarper();

	public DefaultProcessorManager() {
		this.defaultProcessor.processor = DEFAULT_PROCESSOR;
	}

	/**
	 * Non thread safe
	 */
	@Override
	public void setDefaultProcessor(Processor processor) {
		this.defaultProcessor.processor = processor;
	}

	@Override
	public ProcessorManager addLast(String name, Processor processor) throws DuplicateProcessorException {
		synchronized (warpers) {
			for (int i = 0; i < index; i++) {
				if (warpers[i].name.equals(name)) {
					throw new DuplicateProcessorException("Processor with name '" + name + "' has already in manager");
				}
			}
			if (index < warpers.length) {
				warpers[index] = new ProcessorWarper();
				warpers[index].name = name;
				warpers[index].processor = processor;
				index++;
			} else {
				ProcessorWarper tmp[] = new ProcessorWarper[warpers.length * 2];
				System.arraycopy(warpers, 0, tmp, 0, index);
				warpers = tmp;
				warpers[index] = new ProcessorWarper();
				warpers[index].name = name;
				warpers[index].processor = processor;
				index++;
			}
		}
		return this;
	}

	@Override
	public ProcessorManager addBefore(String beforeName, String name, Processor processor)
			throws NotFoundProcessorException {
		synchronized (warpers) {
			for (int i = 0; i < index; i++) {
				if (warpers[i].name.equals(name)) {
					throw new DuplicateProcessorException("Processor with name '" + name + "' has already in manager");
				}
			}
			int pre = -1;
			for (int i = 0; i < index; i++) {
				if (warpers[i].name.equals(beforeName)) {
					pre = i;
					break;
				}
			}
			if (pre == -1)
				throw new NotFoundProcessorException("Processor with name '" + beforeName + "' not found");

			if (pre < warpers.length) {
				System.arraycopy(warpers, pre, warpers, pre + 1, index - pre);

				warpers[pre] = new ProcessorWarper();
				warpers[pre].name = name;
				warpers[pre].processor = processor;
				index++;
			} else {
				ProcessorWarper tmp[] = new ProcessorWarper[warpers.length * 2];
				System.arraycopy(warpers, 0, tmp, 0, pre);
				System.arraycopy(warpers, pre, tmp, pre + 1, index - pre);

				warpers = tmp;
				warpers[pre] = new ProcessorWarper();
				warpers[pre].name = name;
				warpers[pre].processor = processor;
				index++;
			}
		}
		return this;
	}

	@Override
	public ProcessorManager replace(String beforeName, String nowName, Processor processor)
			throws NotFoundProcessorException, DuplicateProcessorException {
		synchronized (warpers) {
			for (int i = 0; i < index; i++) {
				if (warpers[i].name.equals(nowName)) {
					throw new DuplicateProcessorException(
							"Processor with name '" + nowName + "' has already in manager");
				}
			}
			int pre = -1;
			for (int i = 0; i < index; i++) {
				if (warpers[i].name.equals(beforeName)) {
					pre = i;
					break;
				}
			}
			if (pre == -1)
				throw new NotFoundProcessorException("Processor with name '" + beforeName + "' not found");

			warpers[pre].name = nowName;
			warpers[pre].processor = processor;
		}
		return this;
	}

	@Override
	public ProcessorManager remove(String name) throws NotFoundProcessorException {
		synchronized (warpers) {
			int pre = -1;
			for (int i = 0; i < index; i++) {
				if (warpers[i].name.equals(name)) {
					pre = i;
					break;
				}
			}
			if (pre == -1)
				throw new NotFoundProcessorException("Processor with name '" + name + "' not found");

			System.arraycopy(warpers, pre + 1, warpers, pre, index - pre - 1);
			index--;
		}
		return this;
	}

	@Override
	public boolean handleChannelStateChanged(ProcessorContext context, State pre, State cur) throws ProcessException {
		synchronized (warpers) {
			for (int i = 0; i < index; i++) {
				ProcessorWarper warper = warpers[i];
				if (warper.processor.handleChannelStateChanged(context, pre, cur)) {
					return true;
				}
			}
		}
		if (defaultProcessor.processor != null
				&& defaultProcessor.processor.handleChannelStateChanged(context, pre, cur)) {
			return true;
		}
		return false;
	}

	@Override
	public <T extends Serializable> boolean handleMessage(ProcessorContext context, Message<T> msg)
			throws ProcessException {
		synchronized (warpers) {
			for (int i = 0; i < index; i++) {
				ProcessorWarper warper = warpers[i];
				if (warper.processor.handleMessage(context, msg)) {
					return true;
				}
			}
		}
		if (defaultProcessor.processor != null && defaultProcessor.processor.handleMessage(context, msg)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean handleEvent(ProcessorContext context, Object event) throws ProcessException {
		synchronized (warpers) {
			for (int i = 0; i < index; i++) {
				ProcessorWarper warper = warpers[i];
				if (warper.processor.handleEvent(context, event)) {
					return true;
				}
			}
		}
		if (defaultProcessor.processor != null && defaultProcessor.processor.handleEvent(context, event)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean handleException(ProcessorContext context, Throwable t) throws ProcessException {
		synchronized (warpers) {
			for (int i = 0; i < index; i++) {
				ProcessorWarper warper = warpers[i];
				if (warper.processor.handleException(context, t)) {
					return true;
				}
			}
		}
		if (defaultProcessor.processor != null && defaultProcessor.processor.handleException(context, t)) {
			return true;
		}
		return false;
	}

	class ProcessorWarper {
		String name;
		Processor processor;
	}

	static class DefaultMessageProcessor implements Processor {
		private static final Logger logger = LoggerFactory.getLogger(DefaultMessageProcessor.class);

		@Override
		public <T extends Serializable> boolean handleMessage(ProcessorContext context, Message<T> msg)
				throws ProcessException {
			logger.warn("Default processor caughted non-process process", context.getChannelHandlerContext(), msg);
			return true;
		}

		@Override
		public boolean handleChannelStateChanged(ProcessorContext context, State pre, State cur)
				throws ProcessException {
			logger.warn("Default processor caughted non-process stateChanged, {}, {} -> {}",
					context.getChannelHandlerContext(), pre, cur);
			return true;
		}

		@Override
		public boolean handleEvent(ProcessorContext context, Object event) throws ProcessException {
			logger.warn("Default processor caughted non-process event {}, {}", context.getChannelHandlerContext(),
					event);
			return true;
		}

		@Override
		public boolean handleException(ProcessorContext context, Throwable t) throws ProcessException {
			logger.warn("Default processor caughted non-process exception, " + context.getChannelHandlerContext(), t);
			return true;
		}
	}

}
