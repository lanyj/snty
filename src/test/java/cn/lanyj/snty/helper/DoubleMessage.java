package cn.lanyj.snty.helper;

import java.util.UUID;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.msg.MessageStatus;

public class DoubleMessage implements Message<Double> {
	private static final long serialVersionUID = -3197081867632107900L;

	private String id = UUID.randomUUID().toString();
	private Double content;

	@Override
	public String id() {
		return id;
	}

	@Override
	public Double content() {
		return content;
	}

	@Override
	public MessageStatus status() {
		return MessageStatus.SUCCESS;
	}

	@Override
	public void setContent(Double content) {
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
