package com.example.capt_2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {

   @GetMapping("/accessAdmin")
   @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin() {
        return "Hello, you have accessed an Admin role";
    }
    @GetMapping("/accessUser")
    @PreAuthorize("hasAnyRole('USER')")
    public String accessUser() {
        return "Hello, you have accessed an User role";
    }
    @GetMapping("/accessInvited")
    public String accessInvited() {
        return "Hello, you have accessed an Invited role";
    }
}
