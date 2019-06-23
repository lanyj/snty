package cn.lanyj.snty.common.jedis;

import java.io.Serializable;
import java.util.List;

public interface JedisList<T extends Serializable> extends JedisContainer<T>, List<T> {

}
