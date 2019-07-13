package cn.lanyj.snty.common.msg;

import java.io.Serializable;

public interface Message<T extends Serializable> extends Serializable {

	<F extends Serializable> F id();

	T content();

	MessageStatus status();

	<F extends Message<T>> F setContent(T content);

	<F extends Message<T>> F setStatus(MessageStatus status);

}
