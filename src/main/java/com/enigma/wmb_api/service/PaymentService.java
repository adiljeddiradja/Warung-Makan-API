package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Payment;
import com.enigma.wmb_api.entity.Bill;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    Payment createPayment(Bill bill);
    void checkFailedAndUpdatePayments();
}
