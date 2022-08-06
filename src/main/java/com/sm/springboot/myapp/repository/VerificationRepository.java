package com.sm.springboot.myapp.repository;

import com.sm.springboot.myapp.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends CrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}