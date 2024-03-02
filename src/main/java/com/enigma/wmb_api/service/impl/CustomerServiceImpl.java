package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.spesification.CustomerSpecification;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;


    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer getByid(String id) {
        return findByIdOrThrowNotFound(id) ;
    }

    @Override
    public Page<Customer> getAll(SearchCustomerRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort=Sort.by(Sort.Direction.fromString(request.getDirection()),
                request.getSortBy());

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(),sort);
        Specification<Customer> specification= CustomerSpecification.getSpecification(request);
        return customerRepository.findAll(specification,pageable);
    }
    @Override
    public Customer update(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }


    @Override
    public void deleteById(String id) {
       Customer customer= findByIdOrThrowNotFound(id);
       customerRepository.delete(customer);

    }

    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);
        customerRepository.updateStatus(id, status);

    }
    public Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }
}
