package com.accenture.sast.tracker.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping(value = "/api/status", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> status(@RequestParam(defaultValue = "Welcome") String message) {
        String safeMessage = HtmlUtils.htmlEscape(message);
        String body = "<html><body><h1>" + safeMessage + "</h1>" +
               "<p>Employee Tracker is running.</p></body></html>";
        return ResponseEntity.ok()
                .header("Content-Security-Policy", "default-src 'self'; script-src 'none'; object-src 'none'")
                .body(body);
    }
}
