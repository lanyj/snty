package cn.lanyj.snty.common.msg;

import java.io.Serializable;

public abstract class AbstractMessage<T extends Serializable> implements Message<T> {
	private static final long serialVersionUID = 6379447820078004922L;

	private Serializable id = null;
	private T content = null;
	private MessageStatus status;

	public AbstractMessage(Serializable id) {
		this.id = id;
	}

	public AbstractMessage(Serializable id, T content) {
		this.id = id;
		this.content = content;
		this.status = MessageStatus.SUCCESS;
	}

	public AbstractMessage(Serializable id, T content, MessageStatus status) {
		this.id = id;
		this.content = content;
		this.status = status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <F extends Serializable> F id() {
		return (F) id;
	}

	@Override
	public T content() {
		return content;
	}

	@Override
	public MessageStatus status() {
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <F extends Message<T>> F setContent(T content) {
		this.content = content;
		return (F) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <F extends Message<T>> F setStatus(MessageStatus status) {
		this.status = status;
		return (F) this;
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
			if (!ret)
				return false;
			if (op.content() == null || this.content() == null) {
				ret &= op.content() == this.content();
			} else {
				ret &= op.content().equals(this.content());
			}
			if (!ret)
				return false;
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
		StringBuffer buffer = new StringBuffer(32);
		buffer.append('{');
		buffer.append(this.getClass().getSimpleName());
		buffer.append(": [");
		buffer.append((Serializable) id());
		buffer.append(", ");
		buffer.append(status());
		buffer.append(", ");
		buffer.append(content());
		buffer.append("]}");
		return buffer.toString();
	}
}
