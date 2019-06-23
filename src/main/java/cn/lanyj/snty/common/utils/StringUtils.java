package cn.lanyj.snty.common.utils;

public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean hasNonWhitespaceChar(String str) {
		if (str == null) {
			return false;
		}
		for (int i = 0, len = str.length(); i < len; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

}
