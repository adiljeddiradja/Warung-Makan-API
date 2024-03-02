package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.TransType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository

public interface TransTypeRepository extends JpaRepository <TransType, String> {
}
