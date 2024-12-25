package org.example.controller;

import org.example.model.Event;
import org.example.model.EventRSVP;
import org.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        System.out.println("Returning " + events.size() + " events from the controller");
        events.forEach(event -> System.out.println("Event: " + event.getTitle()));
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event, @RequestParam String registrationCode) {
        // בדיקה אם המשתמש יוצר האירוע הוא רכז והקוד רישום שלו תקין
        if (event.getCreator().isCoordinator() && event.getCreator().getRegistrationCode().equals(registrationCode)) {
            return ResponseEntity.ok(eventService.createEvent(event));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(id, event);
        if (updatedEvent != null) {
            return ResponseEntity.ok(updatedEvent);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        System.out.println("קיבלתי בקשת מחיקה לאירוע עם ID: " + id);
        eventService.deleteEvent(id);
        System.out.println("אירוע עם ID: " + id + " נמחק בהצלחה");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{eventId}/register")
    public ResponseEntity<EventRSVP> registerForEvent(@PathVariable Long eventId, @RequestParam String registrationCode) {
        try {
            return ResponseEntity.ok(eventService.registerForEvent(eventId, registrationCode));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{eventId}/register")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long eventId, @RequestParam String registrationCode) {
        eventService.cancelRegistration(eventId, registrationCode);
        return ResponseEntity.ok().build();
    }
}