package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.models.ClinicAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClinicAmenityRepository extends JpaRepository<ClinicAmenity, Integer> {

    Set<ClinicAmenity> findAllByIdIn(Set<Integer> ids);
}
