package com.votingassistance.repository;

import com.votingassistance.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v.candidateName, COUNT(v) FROM Vote v GROUP BY v.candidateName")
    List<Object[]> countVotes();
}