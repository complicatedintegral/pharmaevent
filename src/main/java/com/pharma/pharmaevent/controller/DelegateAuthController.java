package com.pharma.pharmaevent.controller;

import com.pharma.pharmaevent.model.Delegate;
import com.pharma.pharmaevent.repository.DelegateRepository;
import com.pharma.pharmaevent.repository.ParticipationRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/delegate")
public class DelegateAuthController {

    @Autowired
    private DelegateRepository delegateRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @GetMapping("/login")
    public String login() {
        return "delegate-login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("registrationId") String registrationId,
            HttpSession session,
            Model model) {

        Delegate delegate =
                delegateRepository.findByRegistrationId(registrationId);

        if (delegate == null) {
            model.addAttribute("error", true);
            return "delegate-login";
        }

        session.setAttribute("delegateId", delegate.getId());
        return "redirect:/delegate/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Long delegateId = (Long) session.getAttribute("delegateId");

        if (delegateId == null) {
            return "redirect:/delegate/login";
        }

        Delegate delegate =
                delegateRepository.findById(delegateId).orElse(null);

        if (delegate == null) {
            session.invalidate();
            return "redirect:/delegate/login";
        }

        model.addAttribute("delegate", delegate);
        model.addAttribute(
            "events",
            participationRepository.findByDelegateId(delegateId)
        );

        return "delegate-dashboard";
    }

    @GetMapping("/logout")
    public String delegateLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}