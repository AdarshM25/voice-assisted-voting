package com.votingassistance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.votingassistance.entity")
public class VotingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingBackendApplication.class, args);
    }
}