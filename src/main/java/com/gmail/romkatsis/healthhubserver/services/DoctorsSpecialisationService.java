package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.models.DoctorsSpecialisation;
import com.gmail.romkatsis.healthhubserver.repositories.DoctorsSpecialisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class DoctorsSpecialisationService {

    private final DoctorsSpecialisationRepository doctorsSpecialisationRepository;

    @Autowired
    public DoctorsSpecialisationService(DoctorsSpecialisationRepository doctorsSpecialisationRepository) {
        this.doctorsSpecialisationRepository = doctorsSpecialisationRepository;
    }

    public Set<DoctorsSpecialisation> getDoctorsSpecialisationsSetByIds(Set<Integer> ids) {
        return doctorsSpecialisationRepository.findAllByIdIn(ids);
    }
}
