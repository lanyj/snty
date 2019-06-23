package cn.lanyj.snty.common.jedis;

import java.io.Serializable;
import java.util.Map;

public interface JedisMap<T extends Serializable> extends JedisContainer<T>, Map<String, T> {

}
