package pl.mobilevulcanization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/offer")
    public String offer() {
        return "offer";
    }

    @GetMapping("/companies-offer")
    public String companiesOffer() {
        return "companies-offer";
    }

    @GetMapping("/make-appointment")
    public String makeAppointment() {
        return "make-appointment";
    }

    @GetMapping("/admin-panel")
    public String adminPanel() {
        return "admin-panel";
    }

    @GetMapping("/appointments")
    public String appointments() {
        return "appointments";
    }

    @GetMapping("/dates")
    public String dates() {
        return "dates";
    }

    @GetMapping("/appointment-success")
    public String appointmentSuccess() {
        return "appointment-success";
    }
}
