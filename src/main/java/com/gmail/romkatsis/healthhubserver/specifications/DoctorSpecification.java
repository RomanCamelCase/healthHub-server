package com.gmail.romkatsis.healthhubserver.specifications;

import com.gmail.romkatsis.healthhubserver.enums.Gender;
import com.gmail.romkatsis.healthhubserver.enums.PatientType;
import com.gmail.romkatsis.healthhubserver.models.DoctorSpecialisation;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import com.gmail.romkatsis.healthhubserver.models.Review;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {

    public static Specification<DoctorsDetails> findBy(String city,
                                                       DoctorSpecialisation specialisation,
                                                       PatientType workWith,
                                                       LocalDate workExperience,
                                                       Gender gender,
                                                       Double minRating,
                                                       String sortBy) {
        return ((root, query, criteriaBuilder) -> {
            root.fetch("user", JoinType.LEFT);
            root.fetch("specialisations", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("city"), city));
            predicates.add(criteriaBuilder.isMember(specialisation, root.get("specialisations")));
            predicates.add(criteriaBuilder.equal(root.get("isActive"), true));
            if (workWith != null) predicates.add(criteriaBuilder
                    .equal(root.get("workWith"), workWith));
            if (workExperience != null) predicates.add(criteriaBuilder
                    .lessThanOrEqualTo(root.get("workExperience"), workExperience));
            if (gender != null) predicates.add(criteriaBuilder
                    .equal(root.get("user").get("gender"), gender));
            if (minRating != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("avgRating"), minRating));
            }
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

            query.orderBy(switch (sortBy) {
                case "workExperience" -> criteriaBuilder.asc(root.get("workExperience"));
                case "reviewsAmount" -> {
                    Join<DoctorsDetails, Review> doctorsReviews = root.join("reviews", JoinType.LEFT);
                    query.groupBy(root);
                    yield criteriaBuilder.desc(criteriaBuilder.count(doctorsReviews));
                }
                default -> criteriaBuilder.desc(root.get("avgRating"));
            });
            return query.getRestriction();
        });
    }
}
