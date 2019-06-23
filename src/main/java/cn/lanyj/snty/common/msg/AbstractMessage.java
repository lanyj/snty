package cn.lanyj.snty.common.msg;

import java.io.Serializable;

public abstract class AbstractMessage<T extends Serializable> implements Message<T> {
	private static final long serialVersionUID = 6379447820078004922L;

	private String id = null;
	private T content = null;
	private MessageStatus status;

	public AbstractMessage(String id) {
		this.id = id;
	}

	public AbstractMessage(String id, T content) {
		this.id = id;
		this.content = content;
		this.status = MessageStatus.SUCCESS;
	}

	public AbstractMessage(String id, T content, MessageStatus status) {
		this.id = id;
		this.content = content;
		this.status = status;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public T content() {
		return content;
	}

	@Override
	public MessageStatus status() {
		return status;
	}

	@Override
	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		if (id() == null) {
			return super.hashCode();
		}
		return id().hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		boolean ret = true;
		if (obj instanceof Message) {
			if (!obj.getClass().equals(this.getClass())) {
				return false;
			}
			Message<T> op = (Message<T>) obj;
			if (op.id() == null || this.id() == null) {
				ret &= op.id() == this.id();
			} else {
				ret &= op.id().equals(this.id());
			}
			if (op.content() == null || this.content() == null) {
				ret &= op.content() == this.content();
			} else {
				ret &= op.content().equals(this.content());
			}
			if (op.status() == null || this.status() == null) {
				ret &= op.status() == this.status();
			} else {
				ret &= op.status().equals(this.status());
			}
		} else {
			ret = false;
		}
		return ret;
	}

	@Override
	public String toString() {
		return "[" + id() + ", " + status() + ", " + content() + "]";
	}
}
