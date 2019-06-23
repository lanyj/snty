package cn.lanyj.snty;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import cn.lanyj.snty.common.config.GlobalConfig;
import cn.lanyj.snty.common.config.config.Config;
import cn.lanyj.snty.common.config.config.DefaultConfig;
import cn.lanyj.snty.common.utils.StreamUtils;
import cn.lanyj.snty.helper.SerializerNodeClass;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestGlobalConfig extends TestCase {

	public TestGlobalConfig(String name) {
		super(name);
	}

	public void testSaveProperties() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("A", "123");
		OutputStream stream = StreamUtils.openFileOutputStream("./tmp.cs");
		properties.store(stream, "A-=");
		properties.store(stream, "B-=");
		StreamUtils.closeStream(stream);
	}

	public void testGlobalConfig() throws IOException {
		GlobalConfig config = GlobalConfig.getInstance();
		System.out.println(config.frameBasedConfig.frameBasedMaxFrameLength());
		config.frameBasedConfig.setFrameBasedMaxFrameLength(10240);
		System.out.println(config.frameBasedConfig.frameBasedMaxFrameLength());
	}

	public void testConfigDump() throws IOException {
		SerializerNodeClass n1 = new SerializerNodeClass();
		SerializerNodeClass n2 = new SerializerNodeClass();
		n1.next = n2;
		n2.next = n1;
		n1.value = "Hello world!";
		n2.value = "Hello java !";

		Config config = new DefaultConfig();
		config.putString("A", "123");
		config.putInteger("B", 456);
		config.putSerializable("C", n1);
		config.putBoolean("D", false);

		config.dump("./tmp.cfg");
		config.load("./tmp.cfg");
		Assert.assertEquals("123", config.getString("A"));
		Assert.assertEquals(456, config.getInteger("B").intValue());

		Assert.assertEquals("Hello world!", ((SerializerNodeClass) config.getSerializable("C")).value);
		Assert.assertEquals(false, ((Boolean) config.getBoolean("D")).booleanValue());
	}

}
