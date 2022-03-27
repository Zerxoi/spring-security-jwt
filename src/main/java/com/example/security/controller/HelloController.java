package com.example.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/all")
    public String all() {
        return "hello, all";
    }

    @GetMapping("/spring")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public String hello() {
        return "hello, Spring";
    }
}
