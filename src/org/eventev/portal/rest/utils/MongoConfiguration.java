package org.eventev.portal.rest.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackages = "org.eventev.portal.rest.core.model.repository")
public class MongoConfiguration {

	private static Logger log = LoggerFactory
			.getLogger(MongoConfiguration.class);

	private static Properties properties = new Properties();
	private static ServerAddress seed = null;
	private static List<ServerAddress> seeds = null;
	private static MongoCredential credential = null;
	private static List<MongoCredential> credentials = null;
	private static String database = null;

	static {
		MongoConfiguration.configure();
	}

	public static void configure() {
		try {
			InputStream in = new FileInputStream("mongo.properties");
			properties.load(in);
			in.close();
		} catch (IOException e) {
			log.error("mongo.properties missing!", e);
			System.exit(1);
		}

		database = properties.getProperty("database");

		try {
			if (properties.containsKey("server.address")) {
				// Single Server Setup
				if (properties.containsKey("server.port"))
					seed = new ServerAddress(
							properties.getProperty("server.address"),
							Integer.valueOf(properties
									.getProperty("server.port")));
				else
					seed = new ServerAddress(
							properties.getProperty("server.address"));
			} else if (properties.containsKey("server.0.address")) {
				// Multi Server Setup
				List<ServerAddress> seeds = new ArrayList<ServerAddress>();
				for (int i = 0; properties.containsKey("server." + i
						+ ".address"); i++) {
					if (properties.containsKey("server." + i + ".port"))
						seed = new ServerAddress(
								properties.getProperty("server." + i
										+ ".address"),
								Integer.valueOf(properties
										.getProperty("server." + i + ".port")));
					else
						seed = new ServerAddress(
								properties.getProperty("server." + i
										+ ".address"));
					seeds.add(seed);
				}
			}
			// else Default Setup
		} catch (UnknownHostException e) {
			log.error("Some host in mongo.properties are unknown!", e);
			System.exit(1);
		}

		if (properties.containsKey("db.name")
				&& properties.containsKey("db.user")
				&& properties.containsKey("db.password")) {
			// Single Database Setup
			credential = MongoCredential.createMongoCRCredential(
					properties.getProperty("db.user"),
					properties.getProperty("db.name"),
					properties.getProperty("db.password").toCharArray());
			credentials = Arrays.asList(credential);
		} else if (properties.containsKey("db.0.name")
				&& properties.containsKey("db.0.user")
				&& properties.containsKey("db.0.password")) {
			// Multi Database Setup
			List<MongoCredential> credentials = new ArrayList<MongoCredential>();
			for (int i = 0; properties.containsKey("db." + i + ".name")
					&& properties.containsKey("db." + i + ".user")
					&& properties.containsKey("db." + i + ".password"); i++) {
				credential = MongoCredential.createMongoCRCredential(properties
						.getProperty("db." + i + ".user"), properties
						.getProperty("db." + i + ".name"), properties
						.getProperty("db." + i + ".password").toCharArray());
				credentials.add(credential);
			}
		}
		// else Default Setup
	}

	@Bean
	public static DB mongoDB() throws UnknownHostException {
		return mongoClient().getDB(database);
	}

	@Bean
	public static MongoTemplate mongoTemplate(MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, database);
	}

	@Bean
	public static MongoClient mongoClient() throws UnknownHostException {
		if (seeds == null) {
			if (seed == null)
				return new MongoClient();
			else {
				if (credentials == null)
					return new MongoClient(seed);
				else
					return new MongoClient(seed, credentials);
			}
		} else {
			if (credentials == null)
				return new MongoClient(seeds);
			else
				return new MongoClient(seeds, credentials);
		}
	}

}
