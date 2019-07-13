package cn.lanyj.snty.common.processor.annotation;

import java.lang.reflect.Method;

public class MethodProcessor {
	public static MethodProcessor NULL_PROCESSOR = new MethodProcessor(null);

	public Method method;

	public MethodProcessor() {
	}

	public MethodProcessor(Method method) {
		this.method = method;
	}
}