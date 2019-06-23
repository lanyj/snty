package cn.lanyj.snty.common.utils;

public class StreamUtilsException extends RuntimeException {
	private static final long serialVersionUID = -6962589349356278276L;

	public StreamUtilsException() {
		super();
	}

	public StreamUtilsException(String msg) {
		super(msg);
	}

	public StreamUtilsException(String msg, Throwable e) {
		super(msg, e);
	}
}
