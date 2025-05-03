package com.baraka.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class WebController {
    
    // Serve the search page as the main landing page
    @GetMapping("/")
    public String index(@RequestParam(value = "q", required = false) String query, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            model.addAttribute("initialQuery", query);
        }
        return "search";
    }
    
    // Make the /search path also return the same page
    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String query, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            model.addAttribute("initialQuery", query);
        }
        return "search";
    }
    
    // Handle favorites page (placeholder)
    @GetMapping("/favorites")
    public String favorites() {
        return "search";
    }
    
    // Handle reciters page (placeholder)
    @GetMapping("/reciters")
    public String reciters() {
        return "search";
    }
} 