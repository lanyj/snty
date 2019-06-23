package cn.lanyj.snty.common.processor.annotation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.lanyj.snty.common.processor.Processor;

public class AnnotationBasedProcessorFactory implements Processor {

	private static final Map<Class<?>, AnnotationBasedProcessorContext> CONTEXT = new ConcurrentHashMap<>();

	public static AnnotationBasedProcessor newProcessor(Class<?> clazz, Object bean) {
		AnnotationBasedProcessorContext context = getOrCreateAnnotationBasedProcessorContext(clazz);
		return new AnnotationBasedProcessor(context, bean);
	}

	static AnnotationBasedProcessorContext getOrCreateAnnotationBasedProcessorContext(Class<?> clazz) {
		AnnotationBasedProcessorContext context = CONTEXT.get(clazz);
		if (context == null) {
			context = new AnnotationBasedProcessorContext(clazz);
			CONTEXT.put(clazz, context);
		}
		return context;
	}

}
