package cn.lanyj.snty.common.config.config;

import java.io.IOException;
import java.io.Serializable;

public interface Config {

	void load(String path) throws IOException;

	void dump(String path) throws IOException;
	
	boolean contains(String key);

	void remove(String key);
	
	Boolean getBoolean(String key);

	Integer getInteger(String key);
	
	Long getLong(String key);

	Double getDouble(String key);
	
	String getString(String key);

	<T extends Serializable> T getSerializable(String key);

	Boolean getOrDefaultBoolean(String key, Boolean defaultValue);

	Integer getOrDefaultInteger(String key, Integer defaultValue);

	Long getOrDefaultLong(String key, Long defaultValue);

	Double getOrDefaultDouble(String key, Double defaultValue);

	String getOrDefaultString(String key, String defaultValue);

	<T extends Serializable> T getOrDefaultSerializable(String key, T defaultValue);

	void putBoolean(String key, Boolean value);

	void putInteger(String key, Integer value);

	void putLong(String key, Long value);

	void putDouble(String key, Double value);

	void putString(String key, String value);
	
	void putSerializable(String key, Serializable value);

}
