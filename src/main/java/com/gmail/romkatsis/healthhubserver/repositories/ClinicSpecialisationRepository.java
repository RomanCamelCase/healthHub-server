package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.models.ClinicSpecialisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClinicSpecialisationRepository extends JpaRepository<ClinicSpecialisation, Integer> {

    Set<ClinicSpecialisation> findAllByIdIn(Set<Integer> specialisationIds);
}
