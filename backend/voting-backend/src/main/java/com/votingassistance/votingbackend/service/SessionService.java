package com.votingassistance.votingbackend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SessionService {

    private Map<String, Map<Integer, String>> sessions = new HashMap<>();
    private Map<String, Long> sessionTime = new HashMap<>();
    private Set<String> votedSessions = new HashSet<>();

    // Timeout: 2 minutes (in milliseconds)
    private final long SESSION_TIMEOUT = 2 * 60 * 1000;

    public Map<String, Object> createSession() {
        String sessionId = UUID.randomUUID().toString();

        List<String> candidates = Arrays.asList("Candidate A", "Candidate B", "Candidate C");
        Collections.shuffle(candidates);

        Map<Integer, String> ballot = new HashMap<>();
        for (int i = 0; i < candidates.size(); i++) {
            ballot.put(i + 1, candidates.get(i));
        }

        sessions.put(sessionId, ballot);
        sessionTime.put(sessionId, System.currentTimeMillis());

        Map<String, Object> response = new HashMap<>();
        response.put("session_id", sessionId);
        response.put("ballot", ballot);

        return response;
    }

    public String submitVote(String sessionId, int option) {

        // Check session exists
        if (!sessions.containsKey(sessionId)) {
            return "Invalid session";
        }

        // Check timeout
        long createdTime = sessionTime.get(sessionId);
        long currentTime = System.currentTimeMillis();

        if (currentTime - createdTime > SESSION_TIMEOUT) {
            deleteSession(sessionId);
            return "Session expired";
        }

        // Check already voted
        if (votedSessions.contains(sessionId)) {
            return "Already voted";
        }

        Map<Integer, String> ballot = sessions.get(sessionId);

        if (!ballot.containsKey(option)) {
            return "Invalid option";
        }

        votedSessions.add(sessionId);

        // Optional: delete session after vote
        deleteSession(sessionId);

        return "Vote recorded successfully";
    }

    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
        sessionTime.remove(sessionId);
        votedSessions.remove(sessionId);
    }
}