package cn.lanyj.snty.helper;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.msg.MessageStatus;
import cn.lanyj.snty.common.utils.IdUtils;

public class StrMessage implements Message<String> {
	private static final long serialVersionUID = 3888620854380098047L;

	public String id;
	public String content;

	public StrMessage() {
		this.id = IdUtils.nextUUIDAsString();
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String content() {
		return content;
	}

	@Override
	public MessageStatus status() {
		return MessageStatus.SUCCESS;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void setStatus(MessageStatus status) {
	}

	@Override
	public String toString() {
		return "[" + id() + ", " + status() + ", " + content() + "]";
	}
}