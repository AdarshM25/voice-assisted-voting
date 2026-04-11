package com.votingassistance.votingbackend.model;

import java.util.Map;

public class Session {
    private String sessionId;
    private Map<Integer, String> ballot;

    public Session(String sessionId, Map<Integer, String> ballot) {
        this.sessionId = sessionId;
        this.ballot = ballot;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Map<Integer, String> getBallot() {
        return ballot;
    }
}
