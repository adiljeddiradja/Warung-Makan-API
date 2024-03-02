package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository

public interface BillRepository extends JpaRepository <Bill, String> {

}
