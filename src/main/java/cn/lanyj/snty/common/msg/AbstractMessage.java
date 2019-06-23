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
}
