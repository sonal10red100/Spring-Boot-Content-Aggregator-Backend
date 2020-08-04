package com.sonal.contentaggregator.controller;

import com.sonal.contentaggregator.dto.PostRequest;
import com.sonal.contentaggregator.dto.PostResponse;
import com.sonal.contentaggregator.exceptions.SpringContentAggregatorException;
import com.sonal.contentaggregator.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
        //return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(postRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user/{name}")
    public List<PostResponse> getPostsByUsername(@PathVariable("name") String username) throws SpringContentAggregatorException {
        return postService.getPostsByUsername(username);
    }


}
