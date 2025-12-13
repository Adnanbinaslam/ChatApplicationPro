
package com.chatApplication.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chatApplication.chatapp.model.VerificationToken;
import java.util.Optional;

@Repository
public interface TokenVerificationRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}