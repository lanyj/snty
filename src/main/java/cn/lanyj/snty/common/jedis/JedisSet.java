package cn.lanyj.snty.common.jedis;

import java.io.Serializable;
import java.util.Set;

public interface JedisSet<T extends Serializable> extends JedisContainer<T>, Set<T> {

}
