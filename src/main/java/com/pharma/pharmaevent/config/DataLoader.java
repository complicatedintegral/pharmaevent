package com.pharma.pharmaevent.config;

import com.pharma.pharmaevent.model.Delegate;
import com.pharma.pharmaevent.model.Event;
import com.pharma.pharmaevent.model.Participation;
import com.pharma.pharmaevent.repository.DelegateRepository;
import com.pharma.pharmaevent.repository.EventRepository;
import com.pharma.pharmaevent.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DelegateRepository delegateRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Override
    public void run(String... args) throws Exception {

        if (delegateRepository.count() == 0) {

            // Delegates
            Delegate d1 = new Delegate();
            d1.setName("Gautham Dhinakar");
            d1.setRegistrationId("R001");
            delegateRepository.saveAndFlush(d1);

            Delegate d2 = new Delegate();
            d2.setName("Koushiki Mukherjee");
            d2.setRegistrationId("R002");
            delegateRepository.saveAndFlush(d2);

            // Events
            Event e1 = new Event();
            e1.setEventName("Poster Presentation");
            eventRepository.saveAndFlush(e1);

            Event e2 = new Event();
            e2.setEventName("Workshop");
            eventRepository.saveAndFlush(e2);

            Event e3 = new Event();
            e3.setEventName("Lunch");
            eventRepository.saveAndFlush(e3);

            // Participation
            participationRepository.save(new Participation(d1, e1));
            participationRepository.save(new Participation(d1, e2));
            participationRepository.save(new Participation(d1, e3));
            participationRepository.save(new Participation(d2, e1));
            participationRepository.save(new Participation(d2, e3));

            System.out.println("Dummy data loaded successfully.");
        }
    }
}
