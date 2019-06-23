package cn.lanyj.snty;

import cn.lanyj.snty.common.codec.Serializer;
import cn.lanyj.snty.common.codec.SerializerManager;
import cn.lanyj.snty.helper.SerializerNodeClass;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestSerializer extends TestCase {

	public TestSerializer(String name) {
		super(name);
	}

	public void testSerializer() {
		SerializerNodeClass n1 = new SerializerNodeClass();
		SerializerNodeClass n2 = new SerializerNodeClass();
		n1.next = n2;
		n2.next = n1;
		n1.value = "Hello world!";
		n2.value = "Hello java !";

		Serializer serializer = SerializerManager.getSerializer(SerializerManager.Hessian2);
		byte[] buf = serializer.serialize(n1);
		SerializerNodeClass n3 = serializer.deserialize(buf);
		Assert.assertEquals(n3.value, "Hello world!");
		Assert.assertEquals(n3.next.value, "Hello java !");
		Assert.assertEquals(n3.next.next.value, "Hello world!");
	}

}
