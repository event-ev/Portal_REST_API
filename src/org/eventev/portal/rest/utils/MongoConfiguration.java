package org.eventev.portal.rest.utils;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages = "org.eventev.portal.rest.core.model.repository")
public class MongoConfiguration {

	@Bean
	public MongoTemplate mongoTemplate(MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, "eventev_portal");
	}

	@Bean
	public MongoClient mongoClient() throws UnknownHostException {
		return new MongoClient("localhost");
	}

}
