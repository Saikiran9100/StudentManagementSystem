package com.example.MiniProject.Specification;

import com.example.MiniProject.Entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> getSpecification(
            String tenantId,
            String email,
            Float cGpa) {

        return new Specification<Student>() {

            @Override
            public @Nullable Predicate toPredicate(
                    Root<Student> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();

                list.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                if (email != null && !email.isEmpty()) {
                    list.add(criteriaBuilder.equal(root.get("email"), email));
                }

                if (cGpa != null) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get("cGpa"),
                            cGpa
                    ));
                }

                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
    }
}
