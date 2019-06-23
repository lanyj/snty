package cn.lanyj.snty.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

	private StreamUtils() {
	}

	public static void copy(InputStream src, OutputStream dst) {
		byte[] buf = new byte[4096];
		int read = -1;
		try {
			while ((read = src.read(buf)) != -1) {
				dst.write(buf, 0, read);
			}
			dst.flush();
		} catch (IOException e) {
			throw new StreamUtilsException("Stream IOException", e);
		}
	}

	public static byte[] readAll(InputStream src) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(src, baos);
		return baos.toByteArray();
	}

	public static int read(InputStream src, byte[] buf) {
		return read(src, buf, 0, buf.length);
	}

	public static int read(InputStream src, byte[] buf, int offset, int length) {
		try {
			return src.read(buf, offset, length);
		} catch (IOException e) {
			throw new StreamUtilsException("Stream IOException", e);
		}
	}

	public static void write(OutputStream dst, byte[] buf) {
		write(dst, buf, 0, buf.length);
	}

	public static void write(OutputStream dst, byte[] buf, int offset, int length) {
		try {
			dst.write(buf, offset, length);
		} catch (IOException e) {
			throw new StreamUtilsException("Stream IOException", e);
		}
	}

	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				throw new StreamUtilsException("Close stream exception", e);
			}
		}
	}

	public static FileOutputStream openFileOutputStream(String path) {
		File file = new File(path);
		if (!file.exists()) {
			FileUtils.makeFile(file.getPath());
		}
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// should not be here.
		}
		return null;
	}

	public static FileInputStream openFileInputStream(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// should not be here.
		}
		return null;
	}

}
