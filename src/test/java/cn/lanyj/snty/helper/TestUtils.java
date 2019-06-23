package cn.lanyj.snty.helper;

public class TestUtils {

	public static void sleep(float seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
