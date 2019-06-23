package cn.lanyj.snty.common.processor.exception;

public class NotFoundProcessorException extends RuntimeException {
	private static final long serialVersionUID = 5745297750980723559L;

	public NotFoundProcessorException() {
		super();
	}

	public NotFoundProcessorException(String name) {
		super(name);
	}

}
