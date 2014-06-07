package org.eventev.portal.rest.utils.logging;

import java.net.URI;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.appender.db.nosql.mongodb.MongoDBConnection;
import org.apache.logging.log4j.core.appender.db.nosql.mongodb.MongoDBProvider;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;

public class LoggerConfig {

	static {
		//ConfigurationFactory
		//		.setConfigurationFactory(new Log4J2ConfigurationFactory());
	}

	/**
	 * Just to make JVM visit this class to initialize the static parts.
	 */
	public static void configure() {
	}
	
	public static Level setLevel(Level level) {
		return LoggerConfig.setLevel(LogManager.getLogger(LogManager.ROOT_LOGGER_NAME), level);
	}

	public static Level setLevel(Logger log, Level level) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration conf = ctx.getConfiguration();
		org.apache.logging.log4j.core.config.LoggerConfig lconf = conf.getLoggerConfig(log.getName());
		Level oldLevel = lconf.getLevel();
		lconf.setLevel(level);
		ctx.updateLoggers(conf);
		return oldLevel;
	}

	@Plugin(category = "ConfigurationFactory", name = "Log4J2ConfigurationFactory")
	@Order(0)
	public static class Log4J2ConfigurationFactory extends ConfigurationFactory {

		@Override
		protected String[] getSupportedTypes() {
			return null;
		}

		@Override
		public Configuration getConfiguration(ConfigurationSource source) {
			return new Log4J2Configuration();
		}

		@Override
		public Configuration getConfiguration(String name, URI configLocation) {
			return new Log4J2Configuration();
		}

	}

	private static class Log4J2Configuration extends DefaultConfiguration {

		public Log4J2Configuration() {
			setName("Portal_REST_API");
			getRootLogger().setLevel(Level.INFO);

			NoSQLProvider<MongoDBConnection> noSQLProvider = MongoDBProvider
					.createNoSQLProvider("log", null, null, null, null, null,
							null, null, "MongoConfiguration", null);
			Appender noSQLAppender = NoSQLAppender.createAppender(
					"NoSQLAppender", null, null, "16384", noSQLProvider);
			addAppender(noSQLAppender);

			AppenderRef noSQLAppenderRef = AppenderRef.createAppenderRef(
					"NoSQLAppender", null, null);
			AppenderRef[] appenderRefs = new AppenderRef[1];
			appenderRefs[0] = noSQLAppenderRef;

			Appender asyncAppender = AsyncAppender.createAppender(appenderRefs,
					null, "true", "16384", "AsyncAppender", "true", null, null,
					null);
			addAppender(asyncAppender);
			getRootLogger().addAppender(asyncAppender, null, null);
		}
	}

}
