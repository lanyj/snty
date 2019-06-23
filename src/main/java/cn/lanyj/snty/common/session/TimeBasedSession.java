package cn.lanyj.snty.common.session;

public interface TimeBasedSession extends Session {
	
	void update();
	
	boolean isValidate();
	
}
