package com.sonal.contentaggregator.repository;

import com.sonal.contentaggregator.model.Post;
import com.sonal.contentaggregator.model.User;
import com.sonal.contentaggregator.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
