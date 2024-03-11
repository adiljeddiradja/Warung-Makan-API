package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.request.SearchTablesRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Tables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface TablesService {
    Tables create(Tables tables);

    Tables getByid(String id);

    Page<Tables> getAll(SearchTablesRequest request);


    void deleteById(String id);

}
