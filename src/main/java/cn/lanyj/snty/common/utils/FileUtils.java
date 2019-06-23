package cn.lanyj.snty.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	private FileUtils() {
	}

	public static String getFileParentDirectoryPath(File file) {
		String path = file.getPath();
		int index = path.lastIndexOf(File.separatorChar);
		if (index == -1) {
			return "";
		}
		return path.substring(0, path.lastIndexOf(File.separatorChar));
	}

	public static File makeFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			String dir = getFileParentDirectoryPath(file);
			if (!dir.trim().isEmpty()) {
				File pdir = new File(dir);
				if (!pdir.exists()) {
					pdir.mkdirs();
					try {
						logger.info("Created new directory {} for file {}", pdir.getCanonicalPath(),
								file.getCanonicalPath());
					} catch (IOException e) {
						logger.error("Get file path IOException", e);
					}
				}
			}
			try {
				logger.info("Created new File {}", file.getCanonicalPath());
			} catch (IOException e) {
				logger.error("Get file path IOException", e);
			}
		}
		return file;
	}

	public static boolean isExists(String path) {
		return new File(path).exists();
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
	}

	public static void deleteDirectory(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			deleteDirectory(file);
		}
	}

	public static void pack(String sourceDirPath, String zipFilePath) {
		File p = FileUtils.makeFile(zipFilePath);
		ZipOutputStream zs = new ZipOutputStream(StreamUtils.openFileOutputStream(p.getPath()));

		// used to catch exception in file walker
		Counter counter = new Counter();
		FileUtilsException throwable = new FileUtilsException("Zip file exception");
		try {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
				try {
					zs.putNextEntry(zipEntry);
					Files.copy(path, zs);
					zs.closeEntry();
				} catch (IOException e) {
					throwable.addSuppressed(e);
					counter.increment();
				}
			});
		} catch (Exception e) {
			throwable.addSuppressed(e);
			counter.increment();
		}
		if (!counter.isZero()) {
			throw throwable;
		}
	}

	private static void deleteDirectory(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				deleteDirectory(child);
			}
		} else if (file.isFile()) {
			file.delete();
		}
	}

	private static class Counter {
		int count = 0;

		final void increment() {
			count++;
		}

		final boolean isZero() {
			return count == 0;
		}
	}
}
