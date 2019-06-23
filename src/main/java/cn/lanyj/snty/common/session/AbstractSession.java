package cn.lanyj.snty.common.session;

public abstract class AbstractSession implements Session {
	private static final long serialVersionUID = -8629895427645451654L;
	private final String id;

	public AbstractSession(String id) {
		this.id = id;
	}

	@Override
	public final String id() {
		return id;
	}

	@Override
	public int hashCode() {
		return this.id().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Session) {
			return this.id().equals(((Session) obj).id());
		}
		return false;
	}

}
