

package com.example.MiniProject.Specification;

import com.example.MiniProject.Entity.BankAccount;
import com.example.MiniProject.Entity.Course;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BankAccountSpecification {
    public static Specification<BankAccount> getSpecification(String tenantId, String bankName, String branch) {
        return new Specification<BankAccount>() {
            @Override
            public @Nullable Predicate toPredicate(Root<BankAccount> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();
                list.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                if (bankName != null && !bankName.isEmpty()) {
                    list.add(criteriaBuilder.like(root.get("bankName"), "%" + bankName + "%"));
                }

                if (branch != null && !branch.isEmpty()) {
                    list.add(criteriaBuilder.like(root.get("branch"), "%" + branch + "%"));
                }

                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }

        };
    }
}
