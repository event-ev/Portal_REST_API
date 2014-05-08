package org.eventev.portal.rest.core.model.repository;

import static org.junit.Assert.assertEquals;

import org.eventev.portal.rest.core.model.domain.Organization;
import org.eventev.portal.rest.utils.MongoConfiguration;
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
public class OrganizationRepositoryIntegrationTest {

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private MongoOperations mongo;

	@Before
	public void setup() {
		mongo.dropCollection(Organization.class);
	}

	@After
	public void teardown() {
		mongo.dropCollection(Organization.class);
	}

	@Test
	public void insert() {
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(Organization.class)).count());
		organizationRepository.save(new Organization());
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(Organization.class)).count());
	}
	
	@Test
	public void delete() {
		Organization organization = new Organization();
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(Organization.class)).count());
		organizationRepository.save(organization);
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(Organization.class)).count());
		organizationRepository.delete(organization);
		assertEquals(0, mongo.getCollection(mongo.getCollectionName(Organization.class)).count());
	}
	
	@Test
	public void readAndUpdate() {
		Organization organization = new Organization();
		organization.setName("Öskar");
		organizationRepository.save(organization);
		Organization organization2 = organizationRepository.findOne(organization.getId());
		assertEquals("Öskar", organization2.getName());
		
		organization2.setName("Würßel");
		organizationRepository.save(organization2);
		assertEquals(1, mongo.getCollection(mongo.getCollectionName(Organization.class)).count());
		
		Organization organization3 = organizationRepository.findOne(organization.getId());
		assertEquals("Würßel", organization3.getName());
	}
	
}
