package com.pharma.pharmaevent.repository;

import com.pharma.pharmaevent.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long>{}
