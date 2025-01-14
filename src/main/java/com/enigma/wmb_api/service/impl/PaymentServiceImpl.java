package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.PaymentDetailRequest;
import com.enigma.wmb_api.dto.request.PaymentItemDetailRequest;
import com.enigma.wmb_api.dto.request.PaymentRequest;
import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.entity.Payment;
import com.enigma.wmb_api.repository.PaymentRepository;
import com.enigma.wmb_api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String BASE_URL_SNAP;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository, RestClient restClient,
            @Value("${midtrans.api.key}") String secretKey,
            @Value("${midtrans.api.snap-url}") String baseUrlSnap
    ) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        SECRET_KEY = secretKey;
        BASE_URL_SNAP = baseUrlSnap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(Bill bill) {
        long amount = bill.getBillDetails()
                .stream().mapToLong(value -> (value.getQty() * value.getMenPrice()))
                .reduce(0, Long::sum);

        List<PaymentItemDetailRequest> itemDetailRequestList = bill.getBillDetails().stream()
                .map(billDetail -> PaymentItemDetailRequest.builder()
                        .name(billDetail.getMenu().getName())
                        .price(billDetail.getMenPrice())
                        .quantity(billDetail.getQty())
                        .build()).toList();

        PaymentRequest request = PaymentRequest.builder()
                .paymentDetail(PaymentDetailRequest.builder()
                        .orderId(bill.getId())
                        .amount(amount)
                        .build())
                .paymentItemDetails(itemDetailRequestList)
                .paymentMethod(List.of("shopeepay", "gopay"))
                .build();

        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(BASE_URL_SNAP)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});

        Map<String, String> body = response.getBody();

        Payment payment = Payment.builder()
                .token(body.get("token"))
                .redirectUrl(body.get("redirect_url"))
                .transactionStatus("ordered")
                .build();
        paymentRepository.saveAndFlush(payment);

        return payment;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkFailedAndUpdatePayments() {
        List<String> transactionStatus = List.of("deny", "cancel", "expire", "failure");
        List<Payment> payments = paymentRepository.findAllByTransactionStatusIn(transactionStatus);

        for (Payment payment : payments) {
            for (BillDetail billDetail : payment.getBill().getBillDetails()) {
                Menu menu = billDetail.getMenu();
                menu.setStock(menu.getStock() + billDetail.getQty());
            }
            payment.setTransactionStatus("stock_returned");
        }
    }
}
