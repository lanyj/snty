package cn.lanyj.snty.common.processor.exception;

public class DuplicateProcessorException extends RuntimeException {
	private static final long serialVersionUID = 4613883099793328821L;

	public DuplicateProcessorException() {
		super();
	}
	
	public DuplicateProcessorException(String name) {
		super(name);
	}
	
}
