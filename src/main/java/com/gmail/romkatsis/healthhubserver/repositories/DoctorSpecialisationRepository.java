package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.models.DoctorSpecialisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DoctorSpecialisationRepository extends JpaRepository<DoctorSpecialisation, Integer> {

    Set<DoctorSpecialisation> findAllByIdIn(Set<Integer> ids);
}
