package org.eventev.portal.rest.core.model.repository;

import org.eventev.portal.rest.core.model.domain.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String> {

}
