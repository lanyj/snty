package cn.lanyj.snty.common.utils;

import java.util.UUID;

public class IdUtils {
	
	public static UUID nextUUID() {
		return UUID.randomUUID();
	}

	public static String nextUUIDAsString() {
		return UUID.randomUUID().toString();
	}

}
