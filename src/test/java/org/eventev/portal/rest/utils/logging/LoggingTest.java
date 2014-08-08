package org.eventev.portal.rest.utils.logging;

import static org.junit.Assert.assertEquals;

import org.eventev.portal.rest.utils.mongo.MongoConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfiguration.class })
public class LoggingTest {

	private static Logger log = LoggerFactory.getLogger(LoggingTest.class);

	@Autowired
	private MongoOperations mongo;

	@Before
	public void setup() {
		mongo.dropCollection("log");
	}

	@After
	public void teardown() {
		mongo.dropCollection("log");
	}

	@Test
	public void logInfo() {
		assertEquals(0, mongo.getCollection("log").count());
		log.info("Some Info");
		assertEquals(1, mongo.getCollection("log").count());
	}
	
	@Test
	public void logError() {
		assertEquals(0, mongo.getCollection("log").count());
		log.error("Some Error");
		assertEquals(1, mongo.getCollection("log").count());
	}

}
