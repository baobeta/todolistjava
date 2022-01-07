package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("")
    public String viewPageHome() {
        return "index";
    }
    @GetMapping("/login")
    public String viewPageLogin() {
        return "login";
    }

}
