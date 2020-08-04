package com.sonal.contentaggregator.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.sonal.contentaggregator.dto.PostRequest;
import com.sonal.contentaggregator.dto.PostResponse;
import com.sonal.contentaggregator.model.*;
import com.sonal.contentaggregator.repository.CommentRepository;
import com.sonal.contentaggregator.repository.VoteRepository;
import com.sonal.contentaggregator.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.Optional;

import static com.sonal.contentaggregator.model.VoteType.DOWNVOTE;
import static com.sonal.contentaggregator.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;


    @Mapping( target="createdDate" , expression = "java(java.time.Instant.now())")
    @Mapping(target="description",source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target="voteCount", constant="0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "post.subreddit.name")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer getCommentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
