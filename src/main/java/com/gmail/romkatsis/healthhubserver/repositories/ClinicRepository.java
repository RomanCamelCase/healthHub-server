package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

}
