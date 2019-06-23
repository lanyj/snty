package cn.lanyj.snty.common.jedis.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import cn.lanyj.snty.common.jedis.JedisList;
import cn.lanyj.snty.common.utils.DumpUtils;
import cn.lanyj.snty.common.utils.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ListPosition;

public class DefaultJedisList<T extends Serializable> extends AbstractJedisCollection<T> implements JedisList<T> {

	public DefaultJedisList(String mappedKey) {
		super(mappedKey);
	}

	@Override
	public int size() {
		Jedis jedis = JedisUtils.getJedis();
		long size = jedis.llen(mappedKey());
		jedis.close();
		return (int) size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Serializable) {
			String ec = DumpUtils.serializeAsUrlSafeString((Serializable) o);
			Jedis jedis = JedisUtils.getJedis();
			List<String> vals = jedis.lrange(mappedKey, 0, -1);
			jedis.close();
			return vals.contains(ec);
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		jedis.close();
		List<T> ret = new ArrayList<>(vals.size());
		for (String v : vals) {
			ret.add(DumpUtils.deserializeFromUrlSafeString(v));
		}
		return ret.iterator();
	}

	@Override
	public Object[] toArray() {
		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		jedis.close();

		Object[] ret = new Object[vals.size()];
		int index = 0;
		for (String v : vals) {
			ret[index++] = DumpUtils.deserializeFromUrlSafeString(v);
		}
		return ret;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <T> T[] toArray(T[] a) {
		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		jedis.close();

		if (vals.size() > a.length) {
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), vals.size());
		}
		int index = 0;
		for (String v : vals) {
			a[index++] = DumpUtils.deserializeFromUrlSafeString(v);
		}
		return a;
	}

	@Override
	public boolean add(T e) {
		String ec = DumpUtils.serializeAsUrlSafeString(e);

		Jedis jedis = JedisUtils.getJedis();
		jedis.rpush(mappedKey, ec);
		jedis.close();
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Serializable)) {
			return false;
		}
		Long changed = 0L;
		String ec = DumpUtils.serializeAsUrlSafeString((Serializable) o);
		Jedis jedis = JedisUtils.getJedis();
		changed = jedis.lrem(mappedKey, 1, ec);
		jedis.close();
		return changed > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(Collection<?> c) {
		List<T> cs = new ArrayList<>();
		for (Object o : c) {
			if (o instanceof Serializable) {
				cs.add((T) DumpUtils.serializeAsUrlSafeString((Serializable) o));
			} else {
				return false;
			}
		}

		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		jedis.close();
		return vals.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T o : c) {
			add(o);
		}
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		Jedis jedis = JedisUtils.getJedis();
		String pivot = jedis.lindex(mappedKey, index);
		for (T o : c) {
			String ec = DumpUtils.serializeAsUrlSafeString(o);
			jedis.linsert(mappedKey, ListPosition.AFTER, pivot, ec);
			pivot = ec;
		}
		jedis.close();
		return !c.isEmpty();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		Jedis jedis = JedisUtils.getJedis();
		for (Object p : c) {
			if (p instanceof Serializable) {
				jedis.lrem(mappedKey, 0, DumpUtils.serializeAsUrlSafeString((Serializable) p));
				changed = true;
			}
		}
		jedis.close();

		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		if (c.isEmpty()) {
			changed = !isEmpty();
			clear();
			return changed;
		}
		Set<String> target = new HashSet<>();
		for (Object p : c) {
			target.add(DumpUtils.serializeAsUrlSafeString((Serializable) p));
		}

		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		for (String v : vals) {
			if (!target.contains(v)) {
				if (jedis.lrem(mappedKey, 0, v) > 0) {
					changed = true;
				}
			}
		}
		jedis.close();
		return changed;
	}

	@Override
	public T get(int index) {
		Jedis jedis = JedisUtils.getJedis();
		String val = jedis.lindex(mappedKey, index);
		jedis.close();

		return DumpUtils.deserializeFromUrlSafeString(val);
	}

	@Override
	public T set(int index, T element) {
		if (isLoadPre()) {
			Jedis jedis = JedisUtils.getJedis();
			String pre = jedis.lindex(mappedKey, index);
			jedis.lset(mappedKey, index, DumpUtils.serializeAsUrlSafeString(element));
			jedis.close();
			return DumpUtils.deserializeFromUrlSafeString(pre);
		} else {
			Jedis jedis = JedisUtils.getJedis();
			jedis.lset(mappedKey, index, DumpUtils.serializeAsUrlSafeString(element));
			jedis.close();
			return null;
		}
	}

	@Override
	public void add(int index, T element) {
		String ec = DumpUtils.serializeAsUrlSafeString(element);

		Jedis jedis = JedisUtils.getJedis();
		if (index == 0) {
			jedis.lpush(mappedKey, ec);
		} else {
			String pivot = jedis.lindex(mappedKey, index);
			jedis.linsert(mappedKey, ListPosition.AFTER, pivot, ec);
		}
		jedis.close();
	}

	@Override
	public T remove(int index) {
		Jedis jedis = JedisUtils.getJedis();
		String value = jedis.lindex(mappedKey, index);
		jedis.lrem(mappedKey, 1, value);
		jedis.close();

		if (isLoadPre()) {
			return DumpUtils.deserializeFromUrlSafeString(value);
		} else {
			return null;
		}
	}

	@Override
	public int indexOf(Object o) {
		if (!(o instanceof Serializable)) {
			return -1;
		}
		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		jedis.close();
		return vals.indexOf(DumpUtils.serializeAsUrlSafeString((Serializable) o));
	}

	@Override
	public int lastIndexOf(Object o) {
		if (!(o instanceof Serializable)) {
			return -1;
		}
		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		jedis.close();
		return vals.lastIndexOf(DumpUtils.serializeAsUrlSafeString((Serializable) o));
	}

	@Override
	public ListIterator<T> listIterator() {
		return getAll().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return getAll().listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return getAll().subList(fromIndex, toIndex);
	}

	List<T> getAll() {
		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.lrange(mappedKey, 0, -1);
		jedis.close();

		List<T> ret = new ArrayList<>();
		for (String v : vals) {
			ret.add(DumpUtils.deserializeFromUrlSafeString(v));
		}
		return ret;
	}

}
