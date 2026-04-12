package com.votingassistance;

import com.votingassistance.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Working";
    }
    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("/results")
public Map<String, Long> getResults() {

    Map<String, Long> map = new HashMap<>();

    try {
        List<Object[]> results = voteRepository.countVotes();

        for (Object[] row : results) {
            String name = (String) row[0];

            // SAFE conversion (fixes 500 error)
            Number count = (Number) row[1];

            map.put(name, count.longValue());
        }

    } catch (Exception e) {
        e.printStackTrace(); // 👈 shows error in terminal
    }

    return map;
}
}