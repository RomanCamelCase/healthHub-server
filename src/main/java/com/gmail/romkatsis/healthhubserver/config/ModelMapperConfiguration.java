package com.gmail.romkatsis.healthhubserver.config;

import com.gmail.romkatsis.healthhubserver.dtos.responses.ClinicInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.ClinicInfoShortResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoShortResponse;
import com.gmail.romkatsis.healthhubserver.models.Clinic;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        convertDoctorsDetailsToDoctorInfoShortResponse(modelMapper);
        convertDoctorDetailsToDoctorInfoResponse(modelMapper);
        convertClinicToClinicInfoShortResponse(modelMapper);
        convertClinicToClinicInfoResponse(modelMapper);

        return modelMapper;
    }

    private void convertDoctorsDetailsToDoctorInfoShortResponse(ModelMapper modelMapper) {
        modelMapper.createTypeMap(DoctorsDetails.class, DoctorInfoShortResponse.class)
                .addMapping(details -> details.getUser().getFirstName(),
                        DoctorInfoShortResponse::setFirstName)
                .addMapping(details -> details.getUser().getLastName(),
                        DoctorInfoShortResponse::setLastName)
                .addMapping(DoctorsDetails::getAvgRating, DoctorInfoShortResponse::setRating);
    }

    private void convertDoctorDetailsToDoctorInfoResponse(ModelMapper modelMapper) {
        modelMapper.createTypeMap(DoctorsDetails.class, DoctorInfoResponse.class)
                .addMapping(doctor -> doctor.getUser().getFirstName(),
                        DoctorInfoResponse::setFirstName)
                .addMapping(doctor -> doctor.getUser().getLastName(),
                        DoctorInfoResponse::setLastName)
                .addMapping(doctor -> doctor.getUser().getGender(),
                        DoctorInfoResponse::setGender)
                .addMapping(doctor -> doctor.getUser().getRegistrationDate(),
                        DoctorInfoResponse::setRegistrationDate)
                .addMapping(DoctorsDetails::getAvgRating, DoctorInfoResponse::setRating);
    }

    private void convertClinicToClinicInfoShortResponse(ModelMapper modelMapper) {
        modelMapper.createTypeMap(Clinic.class, ClinicInfoShortResponse.class)
                .addMapping(Clinic::getAvgRating, ClinicInfoShortResponse::setRating);
    }

    private void convertClinicToClinicInfoResponse(ModelMapper modelMapper) {
        modelMapper.createTypeMap(Clinic.class, ClinicInfoResponse.class)
                .addMapping(Clinic::getAvgRating, ClinicInfoResponse::setRating);
    }
}
