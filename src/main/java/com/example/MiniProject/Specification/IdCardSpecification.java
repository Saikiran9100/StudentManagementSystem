package com.example.MiniProject.Specification;

import com.example.MiniProject.Entity.IdCard;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IdCardSpecification {

    public static Specification<IdCard> getSpecification(
            String tenantId,
            Long id,
            String cardNumber,
            Boolean active,
            LocalDate issueDate,
            LocalDate expiryDate,
            Long studentId
    ) {

        return new Specification<IdCard>() {

            @Override
            public @Nullable Predicate toPredicate(
                    Root<IdCard> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();

                list.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                if (id != null) {
                    list.add(criteriaBuilder.equal(root.get("id"), id));
                }

                if (cardNumber != null && !cardNumber.isEmpty()) {
                    list.add(criteriaBuilder.equal(root.get("cardNumber"), cardNumber));
                }

                if (active != null) {
                    list.add(criteriaBuilder.equal(root.get("active"), active));
                }

                if (issueDate != null) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get("issueDate"),
                            issueDate
                    ));
                }

                if (expiryDate != null) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(
                            root.get("expiryDate"),
                            expiryDate
                    ));
                }

                if (studentId != null) {
                    list.add(criteriaBuilder.equal(
                            root.get("student").get("studId"),
                            studentId
                    ));
                }

                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
    }
}
