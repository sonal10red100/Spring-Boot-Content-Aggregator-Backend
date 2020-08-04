package com.sonal.contentaggregator.repository;

import com.sonal.contentaggregator.model.Comment;
import com.sonal.contentaggregator.model.Post;
import com.sonal.contentaggregator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
