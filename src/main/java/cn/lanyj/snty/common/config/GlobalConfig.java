package cn.lanyj.snty.common.config;

import cn.lanyj.snty.common.config.config.Config;
import cn.lanyj.snty.common.config.config.DefaultConfig;
import io.netty.handler.logging.LogLevel;

public class GlobalConfig {
	private static final GlobalConfig instance = new GlobalConfig();

	public final FrameBasedConfig frameBasedConfig;
	public final LoggingConfig loggingConfig;
	public final SessionConfig sessionConfig;
	public final JedisConfig jedisConfig;
	
	public final Config commonConfig = new DefaultConfig();

	private GlobalConfig() {
		this.frameBasedConfig = new FrameBasedConfig();
		this.loggingConfig = new LoggingConfig();
		this.sessionConfig = new SessionConfig();
		this.jedisConfig = new JedisConfig();
	}

	public static final GlobalConfig getInstance() {
		return instance;
	}

	public static class FrameBasedConfig {
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

	}

	public static class LoggingConfig {
		private static final boolean defaultChannelPipeLineUsingLoggingHandler = true;
		private static final LogLevel defaultChannelPipelineLoggingLevel = LogLevel.INFO;

		private boolean channelPipeLineUsingLoggingHandler = defaultChannelPipeLineUsingLoggingHandler;
		private LogLevel channelPipelineLoggingLevel = defaultChannelPipelineLoggingLevel;

		public void setChannelPipeLineUsingLoggingHandler(boolean using) {
			this.channelPipeLineUsingLoggingHandler = using;
		}

		public void setChannelPipeLineLoggingLevel(LogLevel level) {
			this.channelPipelineLoggingLevel = level;
		}

		public boolean channelPipeLineUsingLoggingHandler() {
			return channelPipeLineUsingLoggingHandler;
		}

		public LogLevel channelPipeLineLoggingLevel() {
			return channelPipelineLoggingLevel;
		}

	}

	public static class SessionConfig {
		private static final boolean defaultUsingSession = true;
		private static final long defaultSessionExpiredTimeInMillis = 30 * 1000;

		private boolean usingSession = defaultUsingSession;
		private long sessionExpiredTimeInMillis = defaultSessionExpiredTimeInMillis;

		public void setUsingSession(boolean usingSession) {
			this.usingSession = usingSession;
		}

		public boolean usingSession() {
			return usingSession;
		}

		public void setSessionExpiredTimeInMillis(long millis) {
			this.sessionExpiredTimeInMillis = millis;
		}

		public long sessionExpiredTimeInMillis() {
			return sessionExpiredTimeInMillis;
		}
	}

	public static class JedisConfig {
		private static final String defaultJedisHost = "localhost";
		private static final int defaultJedisPort = 6379;
		private static final int defaultJedisTimeout = 3000;
		private static final String defaultPassword = "";

		private String jedisHost = defaultJedisHost;
		private int jedisPort = defaultJedisPort;
		private int jedisTimeout = defaultJedisTimeout;
		private String password = defaultPassword;

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

	}

}
