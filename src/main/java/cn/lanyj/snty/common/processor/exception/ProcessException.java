package cn.lanyj.snty.common.processor.exception;

public class ProcessException extends RuntimeException {
	private static final long serialVersionUID = 1675180549910884514L;

	public ProcessException() {
		super();
	}
	
	public ProcessException(String msg) {
		super(msg);
	}
	
	public ProcessException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
