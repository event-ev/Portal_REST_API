package org.eventev.portal.rest.core.model.repository;

import org.eventev.portal.rest.core.model.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

}
