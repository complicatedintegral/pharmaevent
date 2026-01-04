package com.pharma.pharmaevent.controller;

import com.pharma.pharmaevent.model.Participation;
import com.pharma.pharmaevent.repository.EventRepository;
import com.pharma.pharmaevent.repository.ParticipationRepository;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    public AdminAuthController(EventRepository eventRepository,
                               ParticipationRepository participationRepository) {
        this.eventRepository = eventRepository;
        this.participationRepository = participationRepository;
    }

    // 1. Login page
    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    // 2. Login submit
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("admin", true);
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("error", "Invalid credentials");
        return "admin-login";
    }

    // 3. Dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("events", eventRepository.findAll());
        return "admin-dashboard";
    }

    // 4. Event attendance
    @GetMapping("/event/{id}")
    public String eventAttendance(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String regId,
            @RequestParam(defaultValue = "all") String status,
            HttpSession session,
            Model model) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        // 1️⃣ Load once
        List<Participation> participations =
                participationRepository.findByEventId(id);

        // 2️⃣ Filter by status
        if ("present".equals(status)) {
            participations = participations.stream()
                .filter(Participation::isChecked)
                .toList();
        } else if ("absent".equals(status)) {
            participations = participations.stream()
                .filter(p -> !p.isChecked())
                .toList();
        }

        // 3️⃣ Filter by name
        if (name != null && !name.isBlank()) {
            participations = participations.stream()
                .filter(p ->
                    p.getDelegate().getName()
                     .toLowerCase()
                     .contains(name.toLowerCase())
                )
                .toList();
        }

        // 4️⃣ Filter by regId
        if (regId != null && !regId.isBlank()) {
            participations = participations.stream()
                .filter(p ->
                    p.getDelegate().getRegistrationId()
                     .toLowerCase()
                     .contains(regId.toLowerCase())
                )
                .toList();
        }

        model.addAttribute("participations", participations);
        model.addAttribute("eventId", id);
        model.addAttribute("name", name);
        model.addAttribute("regId", regId);
        model.addAttribute("status", status);

        return "event-attendance";
    }


    
    // 5. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // 6. Updating attendance
    @PostMapping("/event/update")
    public String updateAttendance(
            @RequestParam Long eventId,
            @RequestParam(required = false) List<Long> presentIds,
            HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        // ✅ Only participations of THIS event
        List<Participation> participations =
                participationRepository.findByEventId(eventId);

        for (Participation p : participations) {
            p.setChecked(
                presentIds != null && presentIds.contains(p.getId())
            );
        }

        participationRepository.saveAll(participations);

        return "redirect:/admin/event/" + eventId;
    }
    
}
