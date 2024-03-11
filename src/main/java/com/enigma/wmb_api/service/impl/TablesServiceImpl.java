package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.request.SearchTablesRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Tables;
import com.enigma.wmb_api.repository.TablesRepository;
import com.enigma.wmb_api.service.TablesService;
import com.enigma.wmb_api.spesification.CustomerSpecification;
import com.enigma.wmb_api.spesification.TablesSpecification;
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
public class TablesServiceImpl implements TablesService {
    private final TablesRepository tablesRepository;
    private final ValidationUtil validationUtil;


    @Override
    public Tables create(Tables tables) {
        return tablesRepository.saveAndFlush(tables);
    }

    @Override
    public Tables getByid(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public Page<Tables> getAll(SearchTablesRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort=Sort.by(Sort.Direction.fromString(request.getDirection()),
                request.getSortBy());

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(),sort);
        Specification<Tables> specification= TablesSpecification.getSpecification(request);
        return tablesRepository.findAll(specification,pageable);
    }

    @Override
    public void deleteById(String id) {
        Tables tables=  findByIdOrThrowNotFound(id);
        tablesRepository.delete(tables);
    }


    public Tables findByIdOrThrowNotFound(String id) {
        return tablesRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }
}
