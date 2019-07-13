package cn.lanyj.snty.common.processor.annotation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.Processor;
import cn.lanyj.snty.common.processor.ProcessorContext;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.exception.DuplicateProcessorException;
import cn.lanyj.snty.common.processor.exception.ProcessException;
import cn.lanyj.snty.common.utils.ReflectUtils;

public class AnnotationBasedProcessor implements Processor {
	AnnotationBasedProcessorContext context;
	Object bean;

	Map<Class<?>, MethodProcessor> messageProcessors = new ConcurrentHashMap<>();
	Map<Class<?>, MethodProcessor> eventProcessors = new ConcurrentHashMap<>();
	Map<Class<?>, MethodProcessor> exceptionProcessors = new ConcurrentHashMap<>();
	Map<Class<?>, MethodProcessor> channelStateChangedProcessors = new ConcurrentHashMap<>();

	public AnnotationBasedProcessor(AnnotationBasedProcessorContext context, Object bean) {
		this.context = context;
		this.bean = bean;
	}

	@Override
	public <T extends Serializable> boolean handleMessage(ProcessorContext context, Message<T> msg)
			throws ProcessException {
		MethodProcessor processor = getMessageProcessor(msg.getClass());
		if (processor.method != null) {
			try {
				return (boolean) processor.method.invoke(bean, context, msg);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new ProcessException("Handle message failed", e);
			}
		}
		return false;
	}

	@Override
	public boolean handleEvent(ProcessorContext context, Object event) throws ProcessException {
		MethodProcessor processor = getEventProcessor(event.getClass());
		if (processor.method != null) {
			try {
				return (boolean) processor.method.invoke(bean, context, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new ProcessException("Handle event failed", e);
			}
		}
		return false;
	}

	@Override
	public boolean handleException(ProcessorContext context, Throwable t) throws ProcessException {
		MethodProcessor processor = getExceptionProcessor(t.getClass());
		if (processor.method != null) {
			try {
				return (boolean) processor.method.invoke(bean, context, t);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new ProcessException("Handle exception failed", e);
			}
		}
		return false;
	}

	@Override
	public boolean handleChannelStateChanged(ProcessorContext context, State pre, State cur) throws ProcessException {
		for (MethodProcessor processor : this.context.getChannelStateChangedProcessors()) {
			try {
				if ((boolean) processor.method.invoke(bean, context, pre, cur)) {
					return true;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new ProcessException("Handle channel state changed failed", e);
			}
		}
		return false;
	}

	public MethodProcessor getMessageProcessor(Class<?> clazz) {
		MethodProcessor processor = messageProcessors.get(clazz);
		if (processor == null) {
			for (MethodProcessor mp : context.getMessageProcessors()) {
				if (clazz.equals(mp.method.getParameterTypes()[1])) {
					processor = mp;
					break;
				}
			}
			if (processor == null) {
				for (MethodProcessor mp : context.getMessageProcessors()) {
					if (ReflectUtils.isClassCastable(clazz, mp.method.getParameterTypes()[1])) {
						if (processor != null) {
							throw new DuplicateProcessorException("Duplicated annotation message processor for type: "
									+ clazz + ", processor: [" + processor.method + ", " + mp.method + "]");
						}
						processor = mp;
					}
				}
			}
			if (processor == null) {
				processor = MethodProcessor.NULL_PROCESSOR;
//				throw new NotFoundProcessorException("Processor not found for message type: " + clazz);
			}
			messageProcessors.put(clazz, processor);
		}
		return processor;
	}

	public MethodProcessor getEventProcessor(Class<?> clazz) {
		MethodProcessor processor = eventProcessors.get(clazz);
		if (processor == null) {
			for (MethodProcessor mp : context.getEventProcessors()) {
				if (clazz.equals(mp.method.getParameterTypes()[1])) {
					processor = mp;
					break;
				}
			}
			if (processor == null) {
				for (MethodProcessor mp : context.getEventProcessors()) {
					if (ReflectUtils.isClassCastable(clazz, mp.method.getParameterTypes()[1])) {
						if (processor != null) {
							throw new DuplicateProcessorException("Duplicated annotation event processor for type: "
									+ clazz + ", processor: [" + processor.method + ", " + mp.method + "]");
						}
						processor = mp;
					}
				}
			}
			if (processor == null) {
				processor = MethodProcessor.NULL_PROCESSOR;
//				throw new NotFoundProcessorException("Processor not found for event type: " + clazz);
			}
			eventProcessors.put(clazz, processor);
		}
		return processor;
	}

	public MethodProcessor getExceptionProcessor(Class<?> clazz) {
		MethodProcessor processor = exceptionProcessors.get(clazz);
		if (processor == null) {
			for (MethodProcessor mp : context.getExceptionProcessors()) {
				if (clazz.equals(mp.method.getParameterTypes()[1])) {
					processor = mp;
					break;
				}
			}
			if (processor == null) {
				for (MethodProcessor mp : context.getExceptionProcessors()) {
					if (ReflectUtils.isClassCastable(clazz, mp.method.getParameterTypes()[1])) {
						if (processor != null) {
							throw new DuplicateProcessorException("Duplicated annotation exception processor for type: "
									+ clazz + ", processor: [" + processor.method + ", " + mp.method + "]");
						}
						processor = mp;
					}
				}
			}
			if (processor == null) {
				processor = MethodProcessor.NULL_PROCESSOR;
//				throw new NotFoundProcessorException("Processor not found for exception type: " + clazz);
			}
			exceptionProcessors.put(clazz, processor);
		}
		return processor;
	}

}
