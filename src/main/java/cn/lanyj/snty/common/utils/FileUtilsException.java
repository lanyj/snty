package cn.lanyj.snty.common.utils;

public class FileUtilsException extends RuntimeException {
	private static final long serialVersionUID = -845403328610666472L;

	public FileUtilsException() {
		super();
	}

	public FileUtilsException(String msg) {
		super(msg);
	}

	public FileUtilsException(String msg, Throwable e) {
		super(msg, e);
	}

}
