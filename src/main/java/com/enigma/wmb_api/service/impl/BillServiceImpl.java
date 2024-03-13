package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.request.UpdateBillRequest;
import com.enigma.wmb_api.dto.response.BillDetailResponse;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.entity.Payment;
import com.enigma.wmb_api.dto.response.PaymentResponse;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.BillRepository;
import com.enigma.wmb_api.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailService billDetailService;
    private final CustomerService customerService;
    private final MenuService menuService;
    private final PaymentService paymentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BillResponse create(BillRequest request) {
        Customer customer = customerService.getByid(request.getCustomerId());

        Bill trx = Bill.builder()
                .customer(customer)
                .transDate(new Date())
                .build();
        billRepository.saveAndFlush(trx);

        List<BillDetail> trxDetails = request.getBillDetail().stream().map(detailRequest -> {
            Menu menu = menuService.getById(detailRequest.getMenuId());

            if (menu.getStock() - detailRequest.getQty() < 0)
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ResponseMessage.ERROR_OUT_OF_STOCK);

            menu.setStock(menu.getStock() - detailRequest.getQty());
            menuService.update(menu);

            return BillDetail.builder()
                    .menu(menu)
                    .bill(trx)
                    .qty(detailRequest.getQty())
                    .menPrice(menu.getPrice())
                    .build();
        }).toList();

        billDetailService.createBulk(trxDetails);
        trx.setBillDetails(trxDetails);

        List<BillDetailResponse> trxDetailResponses = trxDetails.stream().map(detail ->
                BillDetailResponse.builder()
                        .id(detail.getId())
                        .menuId(detail.getMenu().getId())
                        .menuPrice(detail.getMenPrice())
                        .qty(detail.getQty())
                        .build()).toList();

        Payment payment = paymentService.createPayment(trx);
        trx.setPayment(payment);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectUrl())
                .transactionStatus(payment.getTransactionStatus())
                .build();

        return BillResponse.builder()
                .id(trx.getId())
                .customerId(trx.getCustomer().getId())
                .transDate(trx.getTransDate())
                .billDetail(trxDetailResponses)
                .paymentResponse(paymentResponse)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BillResponse> getAll(SearchBillRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Bill> transactions = billRepository.findAll(pageable);

        return transactions.map(trx -> {
            List<BillDetailResponse> trxDetailResponses = trx.getBillDetails().stream().map(detail -> BillDetailResponse.builder()
                    .id(detail.getId())
                    .menuId(detail.getMenu().getId())
                    .menuPrice(detail.getMenPrice())
                    .qty(detail.getQty())
                    .build()).toList();

            return BillResponse.builder()
                    .id(trx.getId())
                    .customerId(trx.getCustomer().getId())
                    .transDate(trx.getTransDate())
                    .billDetail(trxDetailResponses)
                    .build();
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateBillRequest request) {
        Bill transaction = billRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        Payment payment = transaction.getPayment();
        payment.setTransactionStatus(request.getTransactionStatus());
    }
}
