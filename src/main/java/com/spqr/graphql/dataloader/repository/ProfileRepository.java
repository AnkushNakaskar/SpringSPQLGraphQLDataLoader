package com.spqr.graphql.dataloader.repository;

import com.spqr.graphql.dataloader.bean.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ankushnakaskar
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findById(Long id);
    void deleteById(Long id);
}

