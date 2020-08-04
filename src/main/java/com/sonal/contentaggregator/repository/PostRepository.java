package com.sonal.contentaggregator.repository;

import com.sonal.contentaggregator.model.Post;
import com.sonal.contentaggregator.model.Subreddit;
import com.sonal.contentaggregator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);

    Long findByPostName(String postName);

    ;
}
