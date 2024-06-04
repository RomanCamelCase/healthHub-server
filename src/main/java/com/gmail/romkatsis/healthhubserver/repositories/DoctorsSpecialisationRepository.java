package com.gmail.romkatsis.healthhubserver.repositories;

import com.gmail.romkatsis.healthhubserver.models.DoctorsSpecialisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DoctorsSpecialisationRepository extends JpaRepository<DoctorsSpecialisation, Integer> {

    Set<DoctorsSpecialisation> findAllByIdIn(Set<Integer> ids);
}
