package org.eventev.portal.rest.utils.mongo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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

	private static boolean configured = false;
	private static Properties properties = new Properties();
	private static ServerAddress seed = null;
	private static List<ServerAddress> seeds = null;
	private static MongoCredential credential = null;
	private static List<MongoCredential> credentials = null;
	private static String database = null;

	public static void configure() {
		try {
			InputStream in = new FileInputStream(new File(
					MongoConfiguration.class.getResource("/mongo.properties")
							.toURI()));
			properties.load(in);
			in.close();
			log.info("mongo.properties found, loading...");
		} catch (IOException | URISyntaxException e) {
			log.error("mongo.properties missing!", e);
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
			log.error("Some host in mongo.properties is unknown!", e);
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
	public static DB mongoDB() throws UnknownHostException, MissingMongoCredetialsException {
		if (!configured)
			MongoConfiguration.configure();
		return mongoClient().getDB(database);
	}

	@Bean
	public static MongoTemplate mongoTemplate(MongoClient mongoClient) {
		if (!configured)
			MongoConfiguration.configure();
		return new MongoTemplate(mongoClient, database);
	}

	@Bean
	public static MongoClient mongoClient() throws UnknownHostException, MissingMongoCredetialsException {
		if (!configured)
			MongoConfiguration.configure();

		// Get MongoClient
		MongoClient mongoClient = null;
		if (seeds == null) {
			// Single Server Setup
			if (seed == null) {
				// Default Setup (Unauthenticated)
				// NOT ALLOWED
				// mongoClient = new MongoClient();
			} else {
				// Defined Setup
				if (credentials == null) {
					// Unauthenticated
					// NOT ALLOWED
					// mongoClient = new MongoClient(seed);
				} else {
					// Authenticated
					mongoClient = new MongoClient(seed, credentials);
				}
			}
		} else {
			// Multi Server Setup
			if (credentials == null) {
				// Unauthenticated
				// NOT ALLOWED
				// mongoClient = new MongoClient(seeds);
			} else {
				// Authenticated
				mongoClient = new MongoClient(seeds, credentials);
			}
		}

		// Check if we have access
		if(mongoClient == null) {
			MissingMongoCredetialsException e = new MissingMongoCredetialsException();
			log.error("Make sure you have credentials set in mongo.properties", e);
			throw e;
		}
		
		List<String> databases = mongoClient.getDatabaseNames();
		for (String database : databases) {
			DB db = mongoClient.getDB(database);
			if(!db.isAuthenticated());
		}

		return mongoClient;
	}

}
