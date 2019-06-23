package cn.lanyj.snty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import cn.lanyj.snty.common.utils.UncaughtExceptionUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	static {
		UncaughtExceptionUtil.declare();
		Runtime.getRuntime().traceInstructions(true);
		Runtime.getRuntime().traceMethodCalls(true);

		System.out.println("Using workspace: " + System.getProperty("user.dir"));
		System.out.println();
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testListSort() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(4);
		list.add(2);
		list.add(9);
		list.add(0);

		list.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});
		System.out.println(Arrays.toString(list.toArray()));

		list.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		System.out.println(Arrays.toString(list.toArray()));

	}

}
