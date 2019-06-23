package cn.lanyj.snty.common.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;

import cn.lanyj.snty.common.codec.Serializer;
import cn.lanyj.snty.common.codec.SerializerManager;

public class DumpUtils {
	private static final Serializer serializer = SerializerManager.getDefaultSerializer();

	private DumpUtils() {
	}

	public static <T extends Serializable> T readFromInputStream(InputStream stream) {
		byte[] buf = StreamUtils.readAll(stream);
		return serializer.deserialize(buf);
	}

	public static void dumpToOutputStream(Serializable value, OutputStream stream) {
		byte[] buf = serializer.serialize(value);
		StreamUtils.write(stream, buf);
	}

	public static void dumpToFile(Serializable value, String filepath) {
		OutputStream os = StreamUtils.openFileOutputStream(filepath);
		dumpToOutputStream(value, os);
		StreamUtils.closeStream(os);
	}

	public static <T extends Serializable> T readFromFile(String filepath) {
		InputStream is = StreamUtils.openFileInputStream(filepath);
		byte[] buf = StreamUtils.readAll(is);
		StreamUtils.closeStream(is);
		return serializer.deserialize(buf);
	}

	public static String serializeAsUrlSafeString(Serializable value) {
		byte[] buf = serializer.serialize(value);
		return Base64.encodeBase64URLSafeString(buf);
	}

	public static <T extends Serializable> T deserializeFromUrlSafeString(String value) {
		if(value == null) {
			return null;
		}
		byte[] buf = Base64.decodeBase64(value);
		return serializer.deserialize(buf);
	}

}
