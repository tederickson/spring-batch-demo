package com.erickson.spring_batch_demo.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {
    @GetMapping("favicon.ico")
    public void returnNoContent() {
        // Returns 200 OK with no content, satisfying the browser request for missing favorites icon
    }
}

