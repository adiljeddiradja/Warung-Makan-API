package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.constant.TransTypeEnum;
import com.enigma.wmb_api.entity.TransType;
import jakarta.persistence.EnumType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository

public interface TransTypeRepository extends JpaRepository <TransType, String> {
    Optional<TransType> findByTransType(TransTypeEnum transType);
}
