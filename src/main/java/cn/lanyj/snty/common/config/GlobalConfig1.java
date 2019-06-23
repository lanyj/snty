package cn.lanyj.snty.common.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lanyj.snty.common.config.config.Config;
import cn.lanyj.snty.common.config.config.DefaultConfig;
import cn.lanyj.snty.common.utils.FileUtils;
import io.netty.handler.logging.LogLevel;

@Deprecated
class GlobalConfig1 {
	private static final Logger logger = LoggerFactory.getLogger(GlobalConfig1.class);

	private static final GlobalConfig1 instance = new GlobalConfig1("./config.properties");

	public final FrameBasedConfig frameBasedConfig;
	public final LoggingConfig loggingConfig;
	public final SessionConfig sessionConfig;
	public final JedisConfig jedisConfig;

	private final String path;
	private final Config config;

	private GlobalConfig1(String path) {
		this.path = path;
		config = new DefaultConfig();

		this.frameBasedConfig = new FrameBasedConfig(this.config);
		this.loggingConfig = new LoggingConfig(this.config);
		this.sessionConfig = new SessionConfig(this.config);
		this.jedisConfig = new JedisConfig(this.config);
		try {
			reload();
		} catch (IOException e) {
			logger.error("Config load failed", e);
		}
	}

	public synchronized void dump() throws IOException {
		config.dump(path);
	}

	public synchronized void reload() throws IOException {
		if (FileUtils.isExists(path)) {
			config.load(path);
		} else {
			// create config file
			config.dump(path);
			logger.warn("No config file found, created a new config file {}", path);
		}
		update();
	}

	public synchronized Config getConfig() {
		return config;
	}

	public synchronized void update() {
		this.frameBasedConfig.update();
		this.loggingConfig.update();
		this.sessionConfig.update();
		this.jedisConfig.update();
	}

	public synchronized void flush() {
		this.frameBasedConfig.dump();
		this.loggingConfig.dump();
		this.sessionConfig.dump();
		this.jedisConfig.dump();
	}

	public static final GlobalConfig1 getInstance() {
		return instance;
	}

	public static class FrameBasedConfig {
		public static final String FRAME_BASED_MAX_FRAME_LENGTH = "frame_based.max_frame_length";
		public static final String FRAME_BASED_LENGTH_FIELD_OFFSET = "frame_based.length_field_offset";
		public static final String FRAME_BASED_LENGTH_FIELD_LENGTH = "frame_based.length_field_length";
		public static final String FRAME_BASED_LENGTH_ADJUSTMENT = "frame_based.length_adjustment";
		public static final String FRAME_BASED_INITIAL_BYTES_TO_STRIP = "frame_based.initial_bytes_to_strip";

		private static final int defaultFrameBasedMaxFrameLength = 1024 * 4;
		private static final int defaultFrameBasedLengthFieldOffset = 0;
		private static final int defaultFrameBasedLengthFieldLength = 4;
		private static final int defaultFrameBasedLengthAdjustment = 0;
		private static final int defaultFrameBasedInitialBytesToStrip = 4;

		private int frameBasedMaxFrameLength = defaultFrameBasedMaxFrameLength;
		private int frameBasedLengthFieldOffset = defaultFrameBasedLengthFieldOffset;
		private int frameBasedLengthFieldLength = defaultFrameBasedLengthFieldLength;
		private int frameBasedLengthAdjustment = defaultFrameBasedLengthAdjustment;
		private int frameBasedInitialBytesToStrip = defaultFrameBasedInitialBytesToStrip;

		Config config;

		private FrameBasedConfig(Config config) {
			this.config = config;
		}

		public void setFrameBasedMaxFrameLength(int frameBasedMaxFrameLength) {
			this.frameBasedMaxFrameLength = frameBasedMaxFrameLength;
		}

		public void setFrameBasedLengthFieldOffset(int frameBasedLengthFieldOffset) {
			this.frameBasedLengthFieldOffset = frameBasedLengthFieldOffset;
		}

		public void setFrameBasedLengthFieldLength(int frameBasedLengthFieldLength) {
			this.frameBasedLengthFieldLength = frameBasedLengthFieldLength;
		}

		public void setFrameBasedLengthAdjustment(int frameBasedLengthAdjustment) {
			this.frameBasedLengthAdjustment = frameBasedLengthAdjustment;
		}

		public void setFrameBasedInitialBytesToStrip(int frameBasedInitialBytesToStrip) {
			this.frameBasedInitialBytesToStrip = frameBasedInitialBytesToStrip;
		}

		public int frameBasedMaxFrameLength() {
			return frameBasedMaxFrameLength;
		}

		public int frameBasedLengthFieldOffset() {
			return frameBasedLengthFieldOffset;
		}

		public int frameBasedLengthFieldLength() {
			return frameBasedLengthFieldLength;
		}

		public int frameBasedLengthAdjustment() {
			return frameBasedLengthAdjustment;
		}

		public int frameBasedInitialBytesToStrip() {
			return frameBasedInitialBytesToStrip;
		}

		private void update() {
			this.frameBasedMaxFrameLength = config.getOrDefaultInteger(FRAME_BASED_MAX_FRAME_LENGTH,
					defaultFrameBasedMaxFrameLength);
			this.frameBasedLengthFieldOffset = config.getOrDefaultInteger(FRAME_BASED_LENGTH_FIELD_OFFSET,
					defaultFrameBasedLengthFieldOffset);
			this.frameBasedLengthFieldLength = config.getOrDefaultInteger(FRAME_BASED_LENGTH_FIELD_LENGTH,
					defaultFrameBasedLengthFieldLength);
			this.frameBasedLengthAdjustment = config.getOrDefaultInteger(FRAME_BASED_LENGTH_ADJUSTMENT,
					defaultFrameBasedLengthAdjustment);
			this.frameBasedInitialBytesToStrip = config.getOrDefaultInteger(FRAME_BASED_INITIAL_BYTES_TO_STRIP,
					defaultFrameBasedInitialBytesToStrip);
		}

		public void dump() {
			config.putInteger(FRAME_BASED_MAX_FRAME_LENGTH, frameBasedMaxFrameLength);
			config.putInteger(FRAME_BASED_LENGTH_FIELD_OFFSET, frameBasedLengthFieldOffset);
			config.putInteger(FRAME_BASED_LENGTH_FIELD_LENGTH, frameBasedLengthFieldLength);
			config.putInteger(FRAME_BASED_LENGTH_ADJUSTMENT, frameBasedLengthAdjustment);
			config.putInteger(FRAME_BASED_INITIAL_BYTES_TO_STRIP, frameBasedInitialBytesToStrip);
		}
	}

	public static class LoggingConfig {
		public static final String CHANNEL_PIPELINE_USING_LOGGING_HANDLER = "channel_pipeline.using_logging_handler";
		public static final String CHANNEL_PIPELINE_LOGGING_LEVEL = "channel_pipeline.logging_level";

		private static final boolean defaultChannelPipeLineUsingLoggingHandler = true;
		private static final LogLevel defaultChannelPipelineLoggingLevel = LogLevel.INFO;

		private boolean channelPipeLineUsingLoggingHandler = defaultChannelPipeLineUsingLoggingHandler;
		private LogLevel channelPipelineLoggingLevel = defaultChannelPipelineLoggingLevel;

		Config config;

		private LoggingConfig(Config config) {
			this.config = config;
		}

		public void setChannelPipeLineUsingLoggingHandler(boolean using) {
			config.putBoolean(CHANNEL_PIPELINE_USING_LOGGING_HANDLER, using);
		}

		public void setChannelPipeLineLoggingLevel(LogLevel level) {
			config.putString(CHANNEL_PIPELINE_LOGGING_LEVEL, level.name());
		}

		public boolean channelPipeLineUsingLoggingHandler() {
			return channelPipeLineUsingLoggingHandler;
		}

		public LogLevel channelPipeLineLoggingLevel() {
			return channelPipelineLoggingLevel;
		}

		public void update() {
			this.channelPipeLineUsingLoggingHandler = config.getOrDefaultBoolean(CHANNEL_PIPELINE_USING_LOGGING_HANDLER,
					defaultChannelPipeLineUsingLoggingHandler);
			String level = config.getString(CHANNEL_PIPELINE_LOGGING_LEVEL);
			if (level == null) {
				this.channelPipelineLoggingLevel = defaultChannelPipelineLoggingLevel;
			} else {
				this.channelPipelineLoggingLevel = LogLevel.valueOf(level);
			}
		}

		public void dump() {
			config.putBoolean(CHANNEL_PIPELINE_USING_LOGGING_HANDLER, channelPipeLineUsingLoggingHandler);
			config.putString(CHANNEL_PIPELINE_LOGGING_LEVEL, channelPipelineLoggingLevel.name());
		}
	}

	public static class SessionConfig {
		public static final String SESSION_EXPIRED_TIME_IN_MILLIS = "session.expired_time_in_millis";

		private static final long defaultSessionExpiredTimeInMillis = 30 * 1000;

		private long sessionExpiredTimeInMillis = defaultSessionExpiredTimeInMillis;

		Config config;

		private SessionConfig(Config config) {
			this.config = config;
		}

		public void setSessionExpiredTimeInMillis(long millis) {
			this.sessionExpiredTimeInMillis = millis;
		}

		public long sessionExpiredTimeInMillis() {
			return sessionExpiredTimeInMillis;
		}

		public void update() {
			this.sessionExpiredTimeInMillis = config.getOrDefaultLong(SESSION_EXPIRED_TIME_IN_MILLIS,
					defaultSessionExpiredTimeInMillis);
		}

		public void dump() {
			config.putLong(SESSION_EXPIRED_TIME_IN_MILLIS, sessionExpiredTimeInMillis);
		}
	}

	public static class JedisConfig {
		public static final String JEDIS_HOST = "jedis.host";
		public static final String JEDIS_PORT = "jedis.port";
		public static final String JEDIS_TIMEOUT = "jedis.timeout";
		public static final String JEDIS_PASSWORD = "jedis.password";

		private static final String defaultJedisHost = "localhost";
		private static final int defaultJedisPort = 6379;
		private static final int defaultJedisTimeout = 3000;
		private static final String defaultPassword = "";

		private String jedisHost = defaultJedisHost;
		private int jedisPort = defaultJedisPort;
		private int jedisTimeout = defaultJedisTimeout;
		private String password = defaultPassword;

		Config config;

		public JedisConfig(Config config) {
			this.config = config;
		}

		public String jedisHost() {
			return this.jedisHost;
		}

		public void setJedisHost(String host) {
			this.jedisHost = host;
		}

		public int jedisPort() {
			return this.jedisPort;
		}

		public void setJedisPort(int port) {
			this.jedisPort = port;
		}

		public int jedisTimeout() {
			return this.jedisTimeout;
		}

		public void setJedisTimeout(int timeout) {
			this.jedisTimeout = timeout;
		}

		public String jedisPassword() {
			return this.password;
		}

		public void setJedisPassword(String password) {
			this.password = password;
		}

		public void update() {
			this.jedisHost = config.getOrDefaultString(JEDIS_HOST, defaultJedisHost);
			this.jedisPort = config.getOrDefaultInteger(JEDIS_PORT, defaultJedisPort);
			this.jedisTimeout = config.getOrDefaultInteger(JEDIS_TIMEOUT, defaultJedisTimeout);
			this.password = config.getOrDefaultString(JEDIS_PASSWORD, defaultPassword);
		}

		public void dump() {
			config.putString(JEDIS_HOST, defaultJedisHost);
			config.putInteger(JEDIS_PORT, defaultJedisPort);
			config.putInteger(JEDIS_TIMEOUT, defaultJedisTimeout);
			config.putString(JEDIS_PASSWORD, password);
		}
	}

}
