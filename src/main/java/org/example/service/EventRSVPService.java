package org.example.service;

import org.example.model.EventRSVP;
import org.example.repository.EventRSVPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EventRSVPService {
    private final EventRSVPRepository eventRSVPRepository;

    @Autowired
    public EventRSVPService(EventRSVPRepository eventRSVPRepository) {
        this.eventRSVPRepository = eventRSVPRepository;
    }

    public EventRSVP registerForEvent(EventRSVP rsvp) {
        return eventRSVPRepository.save(rsvp);
    }

    public void cancelRegistration(Long id) {
        eventRSVPRepository.deleteById(id);
    }

    public Optional<EventRSVP> getRegistrationByEventAndCode(Long eventId, String registrationCode) {
        return eventRSVPRepository.findByEvent_EventIdAndRegistrationCode(eventId, registrationCode);
    }
}