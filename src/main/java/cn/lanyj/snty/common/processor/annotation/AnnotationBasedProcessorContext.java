package cn.lanyj.snty.common.processor.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cn.lanyj.snty.common.processor.Processor;
import cn.lanyj.snty.common.utils.ReflectUtils;

public class AnnotationBasedProcessorContext {
	final static Method PROCESSOR_METHODS[] = Processor.class.getDeclaredMethods();
	static Method processorHandleMessageMethod = null;
	static Method processorHandleEventMethod = null;
	static Method processorHandleExceptionMethod = null;
	static Method processorHandleChannelStateChangedMethod = null;

	static {
		for (Method method : PROCESSOR_METHODS) {
			if (method.getName().equals("handleMessage")) {
				processorHandleMessageMethod = method;
			} else if (method.getName().equals("handleEvent")) {
				processorHandleEventMethod = method;
			} else if (method.getName().equals("handleException")) {
				processorHandleExceptionMethod = method;
			} else if (method.getName().equals("handleChannelStateChanged")) {
				processorHandleChannelStateChangedMethod = method;
			}
		}
	}

	List<MethodProcessor> messageProcessors = new ArrayList<>();
	List<MethodProcessor> eventProcessors = new ArrayList<>();
	List<MethodProcessor> exceptionProcessors = new ArrayList<>();
	List<MethodProcessor> channelStateChangedProcessors = new ArrayList<>();

	Class<?> clazz;

	public AnnotationBasedProcessorContext(Class<?> clazz) {
		this.clazz = clazz;

		validate();
		init();
	}

	void validate() {
		if (!Modifier.isPublic(clazz.getModifiers())) {
			throw new IllegalArgumentException("AnnotationBasedProcessor class can only be modified as PUBLIC");
		}
	}

	void init() {
		List<Method> methods = getAnnotatedMethods();
		for (Method method : methods) {
			MethodProcessor methodProcessor = new MethodProcessor();
			methodProcessor.method = method;

			if (!Modifier.isPublic(method.getModifiers())) {
				throw new IllegalArgumentException("AnnotationBasedProcessor method can only be modified as PUBLIC");
			}

			if (isMessageHandler(method)) {
				messageProcessors.add(methodProcessor);
			} else if (isEventHandler(method)) {
				eventProcessors.add(methodProcessor);
			} else if (isExceptionHandler(method)) {
				exceptionProcessors.add(methodProcessor);
			} else if (isChannelStateChangedHandler(method)) {
				channelStateChangedProcessors.add(methodProcessor);
			}
		}
	}

	List<Method> getAnnotatedMethods() {
		List<Method> ls = new ArrayList<>();
		Method[] ms = clazz.getDeclaredMethods();
		for (Method m : ms) {
			if (m.getAnnotation(ProcessorMethod.class) != null) {
				ls.add(m);
			}
		}
		ls.sort(new Comparator<Method>() {
			@Override
			public int compare(Method o1, Method o2) {
				ProcessorMethod a1 = o1.getAnnotation(ProcessorMethod.class);
				ProcessorMethod a2 = o2.getAnnotation(ProcessorMethod.class);
				return a1.order() - a2.order();
			}
		});
		return ls;
	}

	public List<MethodProcessor> getChannelStateChangedProcessors() {
		return this.channelStateChangedProcessors;
	}

	public List<MethodProcessor> getMessageProcessors() {
		return this.messageProcessors;
	}

	public List<MethodProcessor> getEventProcessors() {
		return this.eventProcessors;
	}

	public List<MethodProcessor> getExceptionProcessors() {
		return this.exceptionProcessors;
	}

	private boolean isMessageHandler(Method method) {
		return ReflectUtils.isMethodCastable(method, processorHandleMessageMethod);
	}

	private boolean isEventHandler(Method method) {
		return ReflectUtils.isMethodCastable(method, processorHandleEventMethod);
	}

	private boolean isExceptionHandler(Method method) {
		return ReflectUtils.isMethodCastable(method, processorHandleExceptionMethod);
	}

	private boolean isChannelStateChangedHandler(Method method) {
		return ReflectUtils.isMethodCastable(method, processorHandleChannelStateChangedMethod);
	}

}
