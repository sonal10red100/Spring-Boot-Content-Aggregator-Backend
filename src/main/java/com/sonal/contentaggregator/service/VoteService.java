package com.sonal.contentaggregator.service;

import com.sonal.contentaggregator.dto.VoteDto;
import com.sonal.contentaggregator.exceptions.PostNotFoundException;
import com.sonal.contentaggregator.exceptions.SpringContentAggregatorException;
import com.sonal.contentaggregator.model.Post;
import com.sonal.contentaggregator.model.Vote;
import com.sonal.contentaggregator.repository.PostRepository;
import com.sonal.contentaggregator.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.sonal.contentaggregator.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) throws SpringContentAggregatorException {
        Post post=postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new PostNotFoundException("Post not found for post id : "+voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,authService.getCurrentUser());
        if(voteByPostAndUser.isPresent() &&
            voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringContentAggregatorException("You have already "+voteDto.getVoteType()+"'d this post");
        }
        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }else{
            post.setVoteCount(post.getVoteCount()-1);
        }
        voteRepository.save(mapToVote(voteDto,post));
        postRepository.save(post);

    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .user(authService.getCurrentUser())
                .post(post).build();
    }
}
