package cn.lanyj.snty.common.config.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.lanyj.snty.common.utils.DumpUtils;
import cn.lanyj.snty.common.utils.StreamUtils;

public class DefaultConfig implements Config {
	Properties nativeMap = new Properties();
	Map<String, Serializable> properties = new HashMap<>();

	@Override
	public synchronized boolean contains(String key) {
		return nativeMap.contains(key);
	}

	@Override
	public synchronized void dump(String path) throws IOException {
		this.nativeMap.store(StreamUtils.openFileOutputStream(path), "Config file");
	}

	@Override
	public synchronized Double getDouble(String key) {
		Serializable value = properties.get(key);
		if (value == null) {
			String sV = nativeMap.getProperty(key);
			if (sV == null) {
				return null;
			}
			value = Double.valueOf(sV);
			properties.put(key, value);
		}
		return (Double) value;
	}

	@Override
	public synchronized Long getLong(String key) {
		Serializable value = properties.get(key);
		if (value == null) {
			String sV = nativeMap.getProperty(key);
			if (sV == null) {
				return null;
			}
			value = Long.valueOf(sV);
			properties.put(key, value);
		}
		return (Long) value;
	}

	@Override
	public synchronized Double getOrDefaultDouble(String key, Double defaultValue) {
		Double val = getDouble(key);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	@Override
	public synchronized Long getOrDefaultLong(String key, Long defaultValue) {
		Long val = getLong(key);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	@Override
	public synchronized <T extends Serializable> T getOrDefaultSerializable(String key, T defaultValue) {
		T val = getSerializable(key);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	@Override
	public synchronized String getOrDefaultString(String key, String defaultValue) {
		String val = getString(key);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T extends Serializable> T getSerializable(String key) {
		Serializable value = properties.get(key);
		if (value == null) {
			String sV = nativeMap.getProperty(key);
			if (sV == null) {
				return null;
			}
			value = DumpUtils.deserializeFromUrlSafeString(sV);
			properties.put(key, value);
		}
		return (T) value;
	}

	@Override
	public synchronized String getString(String key) {
		Serializable value = properties.get(key);
		if (value == null) {
			String sV = nativeMap.getProperty(key);
			if (sV == null) {
				return null;
			}
			value = sV;
			properties.put(key, value);
		}
		return String.valueOf(value);
	}

	@Override
	public synchronized void load(String path) throws IOException {
		this.nativeMap.load(StreamUtils.openFileInputStream(path));
		this.properties.clear();
	}

	@Override
	public synchronized void putDouble(String key, Double value) {
		this.properties.put(key, value);
		this.nativeMap.put(key, value.toString());
	}

	@Override
	public synchronized void putLong(String key, Long value) {
		this.properties.put(key, value);
		this.nativeMap.put(key, value.toString());
	}

	@Override
	public synchronized void putSerializable(String key, Serializable value) {
		this.properties.put(key, value);
		this.nativeMap.put(key, DumpUtils.serializeAsUrlSafeString(value));
	}

	@Override
	public synchronized void putString(String key, String value) {
		this.properties.put(key, value);
		this.nativeMap.put(key, value.toString());
	}

	@Override
	public synchronized void remove(String key) {
		properties.remove(key);
		nativeMap.remove(key);
	}

	@Override
	public synchronized Integer getInteger(String key) {
		Serializable value = properties.get(key);
		if (value == null) {
			String sV = nativeMap.getProperty(key);
			if (sV == null) {
				return null;
			}
			value = Integer.valueOf(sV.toString());
			properties.put(key, value);
		}
		return (Integer) value;
	}

	@Override
	public synchronized Integer getOrDefaultInteger(String key, Integer defaultValue) {
		Integer val = getInteger(key);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	@Override
	public synchronized void putInteger(String key, Integer value) {
		this.nativeMap.setProperty(key, value.toString());
		this.properties.put(key, value);
	}

	@Override
	public synchronized Boolean getBoolean(String key) {
		Serializable value = properties.get(key);
		if (value == null) {
			String sV = nativeMap.getProperty(key);
			if (sV == null) {
				return null;
			}
			value = Boolean.valueOf(sV.toString());
			properties.put(key, value);
		}
		return (Boolean) value;
	}

	@Override
	public synchronized Boolean getOrDefaultBoolean(String key, Boolean defaultValue) {
		Boolean val = getBoolean(key);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	@Override
	public synchronized void putBoolean(String key, Boolean value) {
		this.nativeMap.setProperty(key, value.toString());
		this.properties.put(key, value);
	}

}
