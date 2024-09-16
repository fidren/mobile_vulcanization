package pl.mobilevulcanization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";  // Zwracanie widoku "index.html"
    }

    @GetMapping("/register")
    public String register() {
        return "register";  // Zwracanie widoku "register.html"
    }

    @GetMapping("/login")
    public String login() {
        return "login";  // Zwracanie widoku "login.html"
    }

    @GetMapping("/base")
    public String base() {
        return "base";  // Zwracanie widoku "login.html"
    }

    @GetMapping("/about")
    public String about() {
        return "about";  // Zwracanie widoku "login.html"
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";  // Zwracanie widoku "login.html"
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";  // Zwracanie widoku "login.html"
    }

    @GetMapping("/offer")
    public String offer() {
        return "offer";  // Zwracanie widoku "login.html"
    }

    @GetMapping("/companies-offer")
    public String companiesOffer() {
        return "companies-offer";  // Zwracanie widoku "login.html"
    }

    @GetMapping("/make-appointment")
    public String makeAppointment() {
        return "make-appointment";  // Zwracanie widoku "login.html"
    }
}
