package org.eventev.portal.rest.core.model.repository;

import static org.junit.Assert.assertEquals;

import org.eventev.portal.rest.core.model.domain.Event;
import org.eventev.portal.rest.utils.mongo.MongoConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoConfiguration.class})
public class EventRepositoryIntegrationTest {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private MongoOperations mongo;

	@Before
	public void setup() {
		mongo.dropCollection(Event.class);
	}

	@After
	public void teardown() {
		mongo.dropCollection(Event.class);
	}

	@Test
	public void insert() {
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(Event.class)).count());
		eventRepository.save(new Event());
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(Event.class)).count());
	}
	
	@Test
	public void delete() {
		Event event = new Event();
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(Event.class)).count());
		eventRepository.save(event);
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(Event.class)).count());
		eventRepository.delete(event);
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(Event.class)).count());
	}
	
	@Test
	public void readAndUpdate() {
		Event event = new Event();
		event.setName("Öskar");
		eventRepository.save(event);
		Event event2 = eventRepository.findOne(event.getId());
		assertEquals("Öskar", event2.getName());
		
		event2.setName("Würßel");
		eventRepository.save(event2);
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(Event.class)).count());
		
		Event event3 = eventRepository.findOne(event.getId());
		assertEquals("Würßel", event3.getName());
	}

}
