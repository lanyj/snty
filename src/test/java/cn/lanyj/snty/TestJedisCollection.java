package cn.lanyj.snty;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import cn.lanyj.snty.common.jedis.impl.DefaultJedisList;
import cn.lanyj.snty.common.jedis.impl.DefaultJedisMap;
import cn.lanyj.snty.common.jedis.impl.DefaultJedisSet;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestJedisCollection extends TestCase {

	public TestJedisCollection(String name) {
		super(name);
	}

	public void testJedisList() {
		List<Serializable> list = new DefaultJedisList<>("test");
		list.clear();

		list.add("123");
		list.add(new Dimension(65, 96));
		list.add(null);
		list.add(0, Boolean.TRUE);

		Assert.assertEquals(Boolean.TRUE, list.get(0));
		Assert.assertEquals(4, list.size());
		Assert.assertNull(list.get(3));

		list.remove(1);
		Assert.assertEquals(3, list.size());

		list.forEach(new Consumer<Serializable>() {
			@Override
			public void accept(Serializable t) {
				System.out.println(t);
			}
		});
	}

	public void testJedisSet() {
		Set<Serializable> set = new DefaultJedisSet<>("test");
		set.clear();

		Dimension dimension = new Dimension(58, 76);

		set.add("123");
		set.add(Boolean.FALSE);
		set.add(1024);
		set.add(dimension);

		Assert.assertEquals(set.size(), 4);
		Assert.assertEquals(set.contains(dimension), true);

		set.remove(1024);
		Assert.assertEquals(set.size(), 3);

		set.forEach(new Consumer<Serializable>() {
			@Override
			public void accept(Serializable t) {
				System.out.println(t);
			}
		});
	}

	public void testJedisMap() {
		Map<String, Serializable> map = new DefaultJedisMap<>("test");
		map.clear();

		map.put("a", "A");
		map.put("b", "B");
		map.put("c", "C");

		Assert.assertEquals(map.get("b"), "B");
		Assert.assertEquals(map.size(), 3);

		map.remove("b");
		Assert.assertNull(map.get("b"));
		Assert.assertEquals(map.size(), 2);

		map.clear();
		Assert.assertEquals(map.size(), 0);

		map.put("a", "A");
		map.put("b", "B");
		map.put("c", "C");
		map.put("d", 1024);
		map.put("e", false);
		map.put("f", null);

		map.forEach(new BiConsumer<String, Serializable>() {
			@Override
			public void accept(String t, Serializable u) {
				System.out.println(String.format("%s\t%s", t, u));
			}
		});

		map.clear();
	}

}
