package org.example.controller;

import org.example.model.EventRSVP;
import org.example.service.EventRSVPService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

    @RestController
    @RequestMapping("/api/rsvp")
    public class EventRSVPController {
        private final EventRSVPService eventRSVPService;

        public EventRSVPController(EventRSVPService eventRSVPService) {
            this.eventRSVPService = eventRSVPService;
        }

        @PostMapping
        public EventRSVP registerForEvent(@RequestBody EventRSVP rsvp) {
            return eventRSVPService.registerForEvent(rsvp);
        }

        @DeleteMapping("/{id}")
        public void cancelRegistration(@PathVariable Long id) {
            eventRSVPService.cancelRegistration(id);
        }

        @GetMapping("/event/{eventId}/code/{registrationCode}")
        public Optional<EventRSVP> getRegistrationByEventAndCode(
                @PathVariable Long eventId, @PathVariable String registrationCode) {
            return eventRSVPService.getRegistrationByEventAndCode(eventId, registrationCode);
        }
    }

