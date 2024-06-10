package com.gmail.romkatsis.healthhubserver.specifications;

import com.gmail.romkatsis.healthhubserver.models.Clinic;
import com.gmail.romkatsis.healthhubserver.models.ClinicAmenity;
import com.gmail.romkatsis.healthhubserver.models.ClinicReview;
import com.gmail.romkatsis.healthhubserver.models.ClinicSpecialisation;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClinicSpecification {

    public static Specification<Clinic> findBy(String city,
                                               ClinicSpecialisation specialisation,
                                               Boolean isPrivate,
                                               Set<ClinicAmenity> amenities,
                                               Double minRating,
                                               String sortBy) {
        return ((root, query, criteriaBuilder) -> {
            root.fetch("specialisations");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("city"), city));
            predicates.add(criteriaBuilder.isMember(specialisation, root.get("specialisations")));
            if (isPrivate != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPrivate"), isPrivate));
            }
            if (amenities != null) {
                for (ClinicAmenity amenity : amenities) {
                    predicates.add(criteriaBuilder.isMember(amenity, root.get("amenities")));
                }
            }
            if (minRating != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("avgRating"), minRating));
            }
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
            query.orderBy(switch (sortBy) {
                case "reviewsAmount" -> {
                    Join<Clinic, ClinicReview> clinicReviews = root.join("reviews", JoinType.LEFT);
                    query.groupBy(root.get("id"));
                    yield criteriaBuilder.desc(criteriaBuilder.count(clinicReviews));
                }
                default -> criteriaBuilder.desc(root.get("avgRating"));
            });
            return query.getRestriction();
        });
    }
}
