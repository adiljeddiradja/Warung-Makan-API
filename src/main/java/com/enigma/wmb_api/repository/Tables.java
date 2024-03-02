package com.enigma.wmb_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface Tables extends JpaRepository <Tables, String> {
}
