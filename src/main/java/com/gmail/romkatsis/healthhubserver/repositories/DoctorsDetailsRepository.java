package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DoctorsDetailsRepository extends JpaRepository<DoctorsDetails, Integer>,
        JpaSpecificationExecutor<DoctorsDetails> {

    Optional<DoctorsDetails> findByUserId(Integer id);
}
