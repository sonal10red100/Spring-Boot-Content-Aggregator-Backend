package com.sonal.contentaggregator.service;

import com.sonal.contentaggregator.dto.CommentDto;
import com.sonal.contentaggregator.exceptions.PostNotFoundException;
import com.sonal.contentaggregator.exceptions.SpringContentAggregatorException;
import com.sonal.contentaggregator.mapper.CommentMapper;
import com.sonal.contentaggregator.model.Comment;
import com.sonal.contentaggregator.model.NotificationEmail;
import com.sonal.contentaggregator.model.Post;
import com.sonal.contentaggregator.model.User;
import com.sonal.contentaggregator.repository.CommentRepository;
import com.sonal.contentaggregator.repository.PostRepository;
import com.sonal.contentaggregator.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    public void save(CommentDto commentDto) throws SpringContentAggregatorException {
        Post post=postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new PostNotFoundException("Post not found for post id: "+commentDto.getPostId().toString()));
        User user=authService.getCurrentUser();
        Comment comment=commentMapper.map(commentDto,post,user);
        commentRepository.save(comment);

        //send email to notify about the posted comment to the owner of the post
        String message=mailContentBuilder.build(post.getUser().getUsername()+" commented on your post : ");
        sendCommentNotification(message,post.getUser());
    }

    private void sendCommentNotification(String message, User user) throws SpringContentAggregatorException {
        System.out.println("USer email id : "+user.getEmail());
        mailService.sendEmail(new NotificationEmail("Somebody commented on your post!",user.getEmail(),message));
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post=postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found for this id : "+postId));
        List<Comment> comments=commentRepository.findByPost(post);
        return comments.stream().map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentDto> getAllCommentsForUser(String userName) {
        User user=userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+userName));
        System.out.println("+++++++++++++++++++++++"+user);
        List<Comment> comments=commentRepository.findAllByUser(user);
        return comments.stream().map(commentMapper::mapToDto).collect(toList());
    }
}
