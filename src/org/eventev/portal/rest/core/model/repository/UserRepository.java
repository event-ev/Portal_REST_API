package org.eventev.portal.rest.core.model.repository;

import org.eventev.portal.rest.core.model.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
