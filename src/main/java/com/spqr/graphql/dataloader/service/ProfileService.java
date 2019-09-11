package com.spqr.graphql.dataloader.service;

import com.spqr.graphql.dataloader.bean.Profile;
import com.spqr.graphql.dataloader.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author ankushnakaskar
 */
@Service
@Slf4j
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> profiles() {
        log.info("Fetching all the profiles ....!!!!");
        return StreamSupport.stream(profileRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

}
