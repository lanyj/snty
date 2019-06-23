package cn.lanyj.snty.common.lifecycle;

import java.util.ArrayList;
import java.util.List;

public class NettyLifeCycleManager implements NettyLifeCycle {

	List<NettyLifeCycle> lifeCycles = new ArrayList<>();

	public void addLifeCycleListener(NettyLifeCycle lifeCycle) {
		this.lifeCycles.add(lifeCycle);
	}

	@Override
	public void startUpSuccess() {
		for (NettyLifeCycle lifeCycle : lifeCycles) {
			lifeCycle.startUpSuccess();
		}
	}

	@Override
	public void startUpFailed(Throwable t) {
		for (NettyLifeCycle lifeCycle : lifeCycles) {
			lifeCycle.startUpFailed(t);
		}
	}

	@Override
	public void shutdownSuccess() {
		for (NettyLifeCycle lifeCycle : lifeCycles) {
			lifeCycle.shutdownSuccess();
		}
	}

	@Override
	public void shutdownFailed(Throwable t) {
		for (NettyLifeCycle lifeCycle : lifeCycles) {
			lifeCycle.shutdownFailed(t);
		}
	}

}
