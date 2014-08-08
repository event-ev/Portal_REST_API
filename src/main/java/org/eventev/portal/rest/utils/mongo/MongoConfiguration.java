package org.eventev.portal.rest.utils.mongo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.CommandFailureException;
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

	public static void configure() throws MissingMongoCredetialsException {
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
			} else {
				// Default Setup not allowed
				MissingMongoCredetialsException e = new MissingMongoCredetialsException();
				log.error("Make sure you have servers set in mongo.properties", e);
				throw e;
			}
		} catch (UnknownHostException e) {
			log.error("Some host in mongo.properties is unknown!", e);
		}

		HashMap<String, String[]> credentialMap = new HashMap<String, String[]>();
		if (properties.containsKey("db.name")
				&& properties.containsKey("db.user")
				&& properties.containsKey("db.password")) {
			// Single Database Setup
			credential = MongoCredential.createMongoCRCredential(
					properties.getProperty("db.user"),
					properties.getProperty("db.name"),
					properties.getProperty("db.password").toCharArray());
			credentials = Arrays.asList(credential);
			credentialMap.put(properties.getProperty("db.name"),
					new String[] { properties.getProperty("db.user"),
							properties.getProperty("db.password") });
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
				credentialMap.put(properties.getProperty("db." + i + ".name"),
						new String[] { properties.getProperty("db." + i + ".user"),
								properties.getProperty("db." + i + ".password") });
			}
		} else {
			// Default Setup not allowed
			MissingMongoCredetialsException e = new MissingMongoCredetialsException();
			log.error("Make sure you have credentials set in mongo.properties",
					e);
			throw e;
		}
		
		MongoConfiguration.trySetupAuthentication(credentials, credentialMap);
	}

	private static void trySetupAuthentication(List<MongoCredential> credentials, HashMap<String, String[]> credentialMap) {
		MongoClient mongoClient;
		if(seeds == null)
			mongoClient = new MongoClient(seed, credentials);
		else
			mongoClient = new MongoClient(seeds, credentials);
		
		MongoClient mongoClient2 = null;
		Set<String> databases = credentialMap.keySet();
		for(String database : databases) {
			DB db = mongoClient.getDB(database);
			try {
				db.collectionExists("dummyCollection");
			} catch(CommandFailureException e) {
				if(mongoClient2 == null) {
					if(seeds == null)
						mongoClient2 = new MongoClient(seed);
					else
						mongoClient2 = new MongoClient(seeds);
				}
				DB db2 = mongoClient2.getDB(database);
				String[] credential = credentialMap.get(database);
				db2.addUser(credential[0], credential[1].toCharArray());
			}
		}
	}

	@Bean
	public static DB mongoDB() throws UnknownHostException,
			MissingMongoCredetialsException {
		if (!configured)
			MongoConfiguration.configure();
		return mongoClient().getDB(database);
	}

	@Bean
	public static MongoTemplate mongoTemplate(MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, database);
	}

	@Bean
	public static MongoClient mongoClient() throws UnknownHostException,
			MissingMongoCredetialsException {
		if (!configured)
			MongoConfiguration.configure();

		// Get MongoClient
		MongoClient mongoClient;
		if (seeds == null)
			// Single Server Setup
			mongoClient = new MongoClient(seed, credentials);
		else
			// Multi Server Setup
			mongoClient = new MongoClient(seeds, credentials);
		return mongoClient;
	}

}
