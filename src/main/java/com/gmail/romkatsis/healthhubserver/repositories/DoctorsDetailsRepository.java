package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorsDetailsRepository extends JpaRepository<DoctorsDetails, Integer> {

    Optional<DoctorsDetails> findByUserId(Integer id);
}
