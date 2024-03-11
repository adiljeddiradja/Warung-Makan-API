package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Tables;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository

public interface TablesRepository extends JpaRepository <Tables, String>, JpaSpecificationExecutor {

}
