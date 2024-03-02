package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    Customer create (Customer customer);
    Customer getByid (String id);
    Page<Customer> getAll (SearchCustomerRequest request);

    Customer update(Customer customer);

    void deleteById (String id);
    void updateStatusById(String id, Boolean status);

}
