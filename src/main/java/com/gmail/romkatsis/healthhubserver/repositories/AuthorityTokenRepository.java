package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.enums.Authority;
import com.gmail.romkatsis.healthhubserver.models.AuthorityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityTokenRepository extends JpaRepository<AuthorityToken, Integer> {

    Optional<AuthorityToken> findByTokenAndAuthority(String token, Authority authority);
}
