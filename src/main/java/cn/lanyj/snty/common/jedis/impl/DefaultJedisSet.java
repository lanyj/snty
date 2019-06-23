package cn.lanyj.snty.common.jedis.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cn.lanyj.snty.common.jedis.JedisSet;
import cn.lanyj.snty.common.utils.DumpUtils;

public class DefaultJedisSet<T extends Serializable> extends AbstractJedisCollection<T> implements JedisSet<T> {

	DefaultJedisMap<Boolean> map;

	public DefaultJedisSet(String mappedKey) {
		super(mappedKey);
		this.map = new DefaultJedisMap<>(mappedKey);
		this.map.setLoadPre(isLoadPre());
	}

	@Override
	public void setLoadPre(boolean loadPre) {
		super.setLoadPre(loadPre);
		this.map.setLoadPre(loadPre);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Serializable) {
			return map.containsKey(DumpUtils.serializeAsUrlSafeString((Serializable) o));
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		Set<String> vals = map.keySet();
		Set<T> ret = new HashSet<>();
		for (String v : vals) {
			ret.add(DumpUtils.deserializeFromUrlSafeString(v));
		}
		return ret.iterator();
	}

	@Override
	public Object[] toArray() {
		Set<String> vals = map.keySet();
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
		Set<String> vals = map.keySet();

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
		return map.put(DumpUtils.serializeAsUrlSafeString(e), Boolean.TRUE) == null;
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof Serializable) {
			return map.remove(DumpUtils.serializeAsUrlSafeString((Serializable) o)) != null;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return c.stream().allMatch((t) -> {
			if (c instanceof Serializable) {
				return map.containsKey(DumpUtils.serializeAsUrlSafeString((Serializable) t));
			}
			return false;
		});
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean changed = false;
		for (T p : c) {
			if (map.put(DumpUtils.serializeAsUrlSafeString(p), Boolean.TRUE)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		if (c.isEmpty()) {
			changed = !map.isEmpty();
			map.clear();
			return changed;
		}
		Set<String> target = new HashSet<>();
		for (Object p : c) {
			target.add(DumpUtils.serializeAsUrlSafeString((Serializable) p));
		}
		Set<String> vals = map.keySet();
		for (String v : vals) {
			if (!target.contains(v)) {
				if (map.remove(v)) {
					changed = true;
				}
			}
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (c.isEmpty()) {
			return false;
		}
		boolean changed = false;
		Set<String> target = new HashSet<>();
		for (Object p : c) {
			target.add(DumpUtils.serializeAsUrlSafeString((Serializable) p));
		}
		Set<String> vals = map.keySet();
		for (String v : vals) {
			if (target.contains(v)) {
				if (map.remove(v)) {
					changed = true;
				}
			}
		}
		return changed;
	}

}
