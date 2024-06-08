package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.ReviewDto;
import com.gmail.romkatsis.healthhubserver.dtos.requests.ReviewRequest;
import com.gmail.romkatsis.healthhubserver.models.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReviewService {


    private final DoctorsDetailsService doctorsDetailsService;

    private final ModelMapper modelMapper;

    private final ClinicService clinicService;

    @Autowired
    public ReviewService(DoctorsDetailsService doctorsDetailsService, ModelMapper modelMapper, ClinicService clinicService) {
        this.doctorsDetailsService = doctorsDetailsService;
        this.modelMapper = modelMapper;
        this.clinicService = clinicService;
    }

    public Set<ReviewDto> getDoctorReview(int doctorId) {
        DoctorsDetails doctor = doctorsDetailsService.findDoctorsDetailsById(doctorId);
        return convertReviewSetToReviewDtoSet(doctor.getReviews());
    }

    public Set<ReviewDto> getClinicReviews(int clinicId) {
        Clinic clinic = clinicService.getClinicById(clinicId);
        return convertReviewSetToReviewDtoSet(clinic.getReviews());
    }

    @Transactional
    public Set<ReviewDto> addDoctorReview(int userId, int doctorId, ReviewRequest reviewDto) {
        Set<DoctorReview> reviews = doctorsDetailsService.addDoctorReview(doctorId, userId, reviewDto);
        return convertReviewSetToReviewDtoSet(reviews);
    }

    @Transactional
    public Set<ReviewDto> addClinicReview(int userId, int clinicId, ReviewRequest reviewDto) {
        Set<ClinicReview> reviews = clinicService.addClinicReview(clinicId, userId, reviewDto);
        return convertReviewSetToReviewDtoSet(reviews);
    }

    private Set<ReviewDto> convertReviewSetToReviewDtoSet(Set<? extends Review> reviews) {
        return reviews.stream().map(this::convertReviewToReviewDto).collect(Collectors.toSet());
    }

    private ReviewDto convertReviewToReviewDto(Review review) {
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
        return reviewDto.getAnonymous() ? clearUserFields(reviewDto) : reviewDto;
    }

    private ReviewDto clearUserFields(ReviewDto reviewDto) {
        reviewDto.setUserFirstName(null);
        reviewDto.setUserLastName(null);
        return reviewDto;
    }
}
