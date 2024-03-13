package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,String> {

    // SELECT * FROM t_payment WHERE transaction_status IN ('failed', 'failure', 'cancel', 'deny')
    List<Payment> findAllByTransactionStatusIn(List<String> transactionStatus);


}
