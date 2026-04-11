package com.votingassistance.votingbackend.controller;

import com.votingassistance.votingbackend.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/start-session")
    public Map<String, Object> startSession() {
        return sessionService.createSession();
    }

    @PostMapping("/submit-vote")
    public String submitVote(
            @RequestParam String sessionId,
            @RequestParam int option
    ) {
        return sessionService.submitVote(sessionId, option);
    }
}
