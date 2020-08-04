package com.sonal.contentaggregator.service;

import com.sonal.contentaggregator.dto.PostRequest;
import com.sonal.contentaggregator.dto.PostResponse;
import com.sonal.contentaggregator.exceptions.PostNotFoundException;
import com.sonal.contentaggregator.exceptions.SpringContentAggregatorException;
import com.sonal.contentaggregator.exceptions.SubredditNotFoundException;
import com.sonal.contentaggregator.mapper.PostMapper;
import com.sonal.contentaggregator.model.Post;
import com.sonal.contentaggregator.model.Subreddit;
import com.sonal.contentaggregator.model.User;
import com.sonal.contentaggregator.repository.PostRepository;
import com.sonal.contentaggregator.repository.SubredditRepository;
import com.sonal.contentaggregator.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post save(PostRequest postRequest) {
        Subreddit subreddit=subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found corresponding to this name"+ postRequest.getSubredditName()));
        User user=authService.getCurrentUser();
        return postRepository.save(postMapper.map(postRequest,subreddit,user));
       // Long postId=postRepository.findByPostName(postRequest.getPostName());
       // return postId;
    }

    @Transactional
    public PostResponse getPost(Long id) {
        Post post=postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found for this id : "+id));
        return postMapper.mapToDto(post);
    }

    @Transactional
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional
    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit=subredditRepository.findById(id).orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id : "+id));
        List<Post> postsBySubreddit=postRepository.findBySubreddit(subreddit);
        return postsBySubreddit.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional
    public List<PostResponse> getPostsByUsername(String username) throws SpringContentAggregatorException {
        User user=userRepository.findByUsername(username).orElseThrow(() -> new SpringContentAggregatorException("User not found for username: "+username));
        List<Post> postsByUsername=postRepository.findByUser(user);
        return postsByUsername.stream().map(postMapper::mapToDto).collect(toList());
    }


}
