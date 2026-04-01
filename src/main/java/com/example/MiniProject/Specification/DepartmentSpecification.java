package com.example.MiniProject.Specification;

import com.example.MiniProject.Entity.Department;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DepartmentSpecification {

    public static Specification<Department> getSpecification(String tenantId,
                                                             Long deptId,
                                                             String deptCode,
                                                             String deptName) {

        return new Specification<Department>() {
            @Override
            public @Nullable Predicate toPredicate(Root<Department> root,
                                                   CriteriaQuery<?> query,
                                                   CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();

                list.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                if (deptId != null) {
                    list.add(criteriaBuilder.equal(root.get("deptId"), deptId));
                }
                if (deptCode != null && !deptCode.isEmpty()) {
                    list.add(criteriaBuilder.equal(root.get("deptCode"), deptCode));
                }
                if (deptName != null && !deptName.isEmpty()) {
                    list.add(criteriaBuilder.like(root.get("deptName"), "%" + deptName + "%"));
                }

                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
    }
}
