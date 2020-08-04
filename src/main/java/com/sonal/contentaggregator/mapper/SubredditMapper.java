package com.sonal.contentaggregator.mapper;

import com.sonal.contentaggregator.dto.SubredditDto;
import com.sonal.contentaggregator.model.Post;
import com.sonal.contentaggregator.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
    @Mapping(target = "noOfPosts", expression="java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);
    default Integer mapPosts(List<Post> posts){
        return posts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
