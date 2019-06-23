package cn.lanyj.snty.common.utils;

public class DumpUtilsException extends RuntimeException {
	private static final long serialVersionUID = -845403328610666472L;

	public DumpUtilsException() {
		super();
	}

	public DumpUtilsException(String msg) {
		super(msg);
	}

	public DumpUtilsException(String msg, Throwable e) {
		super(msg, e);
	}
}
