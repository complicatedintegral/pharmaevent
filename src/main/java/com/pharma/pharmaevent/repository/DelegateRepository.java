package com.pharma.pharmaevent.repository;

import com.pharma.pharmaevent.model.Delegate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DelegateRepository extends JpaRepository<Delegate, Long> {

    Delegate findByRegistrationId(String registrationId);
}