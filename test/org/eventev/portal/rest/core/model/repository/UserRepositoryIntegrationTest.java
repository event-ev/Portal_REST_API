package org.eventev.portal.rest.core.model.repository;

import static org.junit.Assert.assertEquals;

import org.eventev.portal.rest.core.model.domain.User;
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
public class UserRepositoryIntegrationTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MongoOperations mongo;

	@Before
	public void setup() {
		mongo.dropCollection(User.class);
	}

	@After
	public void teardown() {
		mongo.dropCollection(User.class);
	}

	@Test
	public void insert() {
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(User.class)).count());
		userRepository.save(new User());
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(User.class)).count());
	}
	
	@Test
	public void delete() {
		User user = new User();
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(User.class)).count());
		userRepository.save(user);
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(User.class)).count());
		userRepository.delete(user);
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(User.class)).count());
	}
	
	@Test
	public void readAndUpdate() {
		User user = new User();
		user.setFirstname("Öskar");
		userRepository.save(user);
		User user2 = userRepository.findOne(user.getId());
		assertEquals("Öskar", user2.getFirstname());
		
		user2.setFirstname("Würßel");
		userRepository.save(user2);
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(User.class)).count());
		
		User user3 = userRepository.findOne(user.getId());
		assertEquals("Würßel", user3.getFirstname());
	}
	
}
