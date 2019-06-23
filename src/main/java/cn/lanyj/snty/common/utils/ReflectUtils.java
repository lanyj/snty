package cn.lanyj.snty.common.utils;

import java.lang.reflect.Method;

import com.google.gson.internal.Primitives;

public class ReflectUtils {

	public static boolean isClassCastable(Class<?> from, Class<?> to) {
		if (Primitives.isPrimitive(from)) {
			from = Primitives.wrap(from);
		}
		if (Primitives.isPrimitive(to)) {
			to = Primitives.wrap(to);
		}
		return to.isAssignableFrom(from);
	}

	public static boolean isObjectCastable(Object obj, Class<?> to) {
		if (obj != null) {
			return isClassCastable(obj.getClass(), to);
		}
		return true;
	}

	public static boolean isMethodCastable(Method from, Method to) {
		if (!isClassCastable(from.getReturnType(), to.getReturnType())
				|| from.getParameterCount() != to.getParameterCount()) {
			return false;
		}
		Class<?> fps[] = from.getParameterTypes();
		Class<?> tps[] = to.getParameterTypes();
		for (int i = 0; i < fps.length; i++) {
			if (!isClassCastable(fps[i], tps[i])) {
				return false;
			}
		}
		return true;
	}

}
