package com.example.MiniProject.Specification;

import com.example.MiniProject.Entity.Course;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseSpecification {

    public static Specification<Course> getSpecification(String tenantId,
                                                         Long courseId,
                                                         String courseName,
                                                         LocalDate deadline) {

        return new Specification<Course>() {
            @Override
            public @Nullable Predicate toPredicate(Root<Course> root,
                                                   CriteriaQuery<?> query,
                                                   CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();

                list.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                if (courseId != null) {
                    list.add(criteriaBuilder.equal(root.get("courseId"), courseId));
                }

                if (courseName != null && !courseName.isEmpty()) {
                    list.add(criteriaBuilder.like(root.get("courseName"), "%" + courseName + "%"));
                }

                if (deadline != null) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(
                            root.get("deadLine"),
                            deadline
                    ));
                }

                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
    }
}
