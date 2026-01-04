package com.pharma.pharmaevent.repository;

import com.pharma.pharmaevent.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findByDelegateId(Long delegateId);

    List<Participation> findByEventId(Long eventId);
    
    List<Participation> findByEventIdAndDelegate_NameContainingIgnoreCase(
            Long eventId,
            String name
    );

    List<Participation> findByEventIdAndDelegate_RegistrationIdContainingIgnoreCase(
            Long eventId,
            String registrationId
    );
    
    List<Participation> findByEventIdAndCheckedTrue(Long eventId);

    List<Participation> findByEventIdAndCheckedFalse(Long eventId);

}
