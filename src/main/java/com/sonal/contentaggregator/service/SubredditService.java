package com.sonal.contentaggregator.service;

import com.sonal.contentaggregator.dto.SubredditDto;
import com.sonal.contentaggregator.exceptions.SpringContentAggregatorException;
import com.sonal.contentaggregator.mapper.SubredditMapper;
import com.sonal.contentaggregator.model.Subreddit;
import com.sonal.contentaggregator.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
       Subreddit save= subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
       subredditDto.setId(save.getId());
       return subredditDto;
    }



    @Transactional
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }


    public SubredditDto getSubreddit(Long id) throws SpringContentAggregatorException {
        Optional<Subreddit> subredditOptional=subredditRepository.findById(id);
        Subreddit subreddit=subredditOptional.orElseThrow(() -> new SpringContentAggregatorException("No subreddit found with id : "+id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
