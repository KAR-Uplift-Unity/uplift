package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.ContactForm;
import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.regex.Pattern;

@Controller
public class HomeController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String returnHomePage(Model model){
        return "index";
    }

    @GetMapping("/mission")
    public String showMissionPage(Model model) {
        return "mission";
    }

    @GetMapping("/contact_us")
    public String showContactUsPage(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contact_us";
    }

    @PostMapping("/contact_us")
    public String handleContactUsForm(@ModelAttribute ContactForm form, Model model) {
        // Email validation
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        if (!emailPattern.matcher(form.getEmail()).matches()) {
            model.addAttribute("emailError", "Please provide a valid email address");
            return "contact_us";
        }

        emailService.sendContactFormEmail(form);
        return "redirect:/contact_us?messageSent=true";
    }
}
