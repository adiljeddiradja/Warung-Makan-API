package com.enigma.wmb_api.spesification;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getSpecification(SearchCustomerRequest request) {
        return (root, cq, cb) -> {

            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                jakarta.persistence.criteria.Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            if (request.getPhoneNumber() != null) {
                jakarta.persistence.criteria.Predicate phonePredicate = cb.equal(root.get("mobilePhoneNo"), request.getPhoneNumber());
                predicates.add(phonePredicate);
            }

            if (request.getStatus() != null) {
                jakarta.persistence.criteria.Predicate statusPredicate = cb.equal(root.get("status"), request.getStatus());
                predicates.add(statusPredicate);
            }

            return cq.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}

