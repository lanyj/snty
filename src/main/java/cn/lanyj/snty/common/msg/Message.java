package cn.lanyj.snty.common.msg;

import java.io.Serializable;

public interface Message<T extends Serializable> extends Serializable {

	String id();

	T content();

	MessageStatus status();

	void setContent(T content);

	void setStatus(MessageStatus status);

}
