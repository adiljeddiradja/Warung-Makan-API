package com.enigma.wmb_api.scheduler;

import com.enigma.wmb_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusScheduler {
    private final PaymentService paymentService;

    @Scheduled
public void checkFailedPayment(){
        log.info("START checkFailedPayments() : {}", System.currentTimeMillis());
        paymentService.checkFailedAndUpdatePayments();
        log.info("END checkFailedPayments() : {}", System.currentTimeMillis());
    }

}
