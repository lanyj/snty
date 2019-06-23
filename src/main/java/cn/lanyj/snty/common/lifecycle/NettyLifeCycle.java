package cn.lanyj.snty.common.lifecycle;

public interface NettyLifeCycle {
	
	void startUpSuccess();
	
	void startUpFailed(Throwable t);
	
	void shutdownSuccess();
	
	void shutdownFailed(Throwable t);
}
