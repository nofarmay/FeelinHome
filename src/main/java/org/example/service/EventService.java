package org.example.service;

import org.example.model.Event;
import org.example.model.EventRSVP;
import org.example.repository.CommunityActivityScoreRepository;
import org.example.repository.EventRSVPRepository;
import org.example.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventRSVPRepository rsvpRepository;
    private final CommunityActivityScoreRepository scoreRepository;

    @Autowired
    public EventService(EventRepository eventRepository,
                        EventRSVPRepository rsvpRepository,
                        CommunityActivityScoreRepository scoreRepository) {
        this.eventRepository = eventRepository;
        this.rsvpRepository = rsvpRepository;
        this.scoreRepository = scoreRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            updatedEvent.setEventId(id);
            return eventRepository.save(updatedEvent);
        }
        return null;
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public EventRSVP registerForEvent(Long eventId, String registrationCode) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.canRegister()) {
            throw new RuntimeException("Cannot register for this event");
        }

        EventRSVP rsvp = new EventRSVP(event, registrationCode);
        return rsvpRepository.save(rsvp);
    }

    public void cancelRegistration(Long eventId, String registrationCode) {
        Optional<EventRSVP> rsvp = rsvpRepository.findByEventIdAndRegistrationCode(eventId, registrationCode);
        rsvp.ifPresent(rsvpRepository::delete);
    }
}