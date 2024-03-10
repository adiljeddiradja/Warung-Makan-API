package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.response.BillDetailResponse;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.BillRepository;
import com.enigma.wmb_api.service.BillDetailService;
import com.enigma.wmb_api.service.BillService;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.service.MenuService;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BillResponse create(BillRequest request) {
        Customer customer = customerService.getByid(request.getCustomerId());

        Bill trx = Bill.builder()
                .customer(customer)
                .transDate(new Date())
                .build();
        billRepository.saveAndFlush(trx);

        List<BillDetail> trxDetail = request.getBillDetail().stream().map(billDetailRequest -> {
            Menu menu = menuService.getById(billDetailRequest.getId());

            if (menu.getStock() - billDetailRequest.getQty() < 0)
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ResponseMessage.ERROR_OUT_OF_STOCK);

            menu.setStock(menu.getStock() - billDetailRequest.getQty());
            menuService.update(menu);

            return BillDetail.builder()
                    .menu(menu)
                    .bill(trx)
                    .qty(billDetailRequest.getQty())
                    .menPrice(menu.getPrice())
                    .build();

        }).toList();

        billDetailService.createBulk(trxDetail);
        trx.setBillDetails(trxDetail);

        List<BillDetailResponse> trxDetailResponses = trxDetail.stream().map(detail ->
                BillDetailResponse.builder()
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
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BillResponse> getAll(SearchBillRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Bill> bills = billRepository.findAll(pageable);
        return bills.map(trx -> {
            List<BillDetailResponse> trxDetailResponses = trx.getBillDetails().stream().map(detail -> BillDetailResponse.builder()
                    .id(detail.getId())
                    .menuId(detail.getMenu().getId())
                    .menuPrice(detail.getMenu().getPrice())
                    .qty(detail.getQty())
                    .build()).toList();

            return new BillResponse().builder()
                    .id(trx.getId())
                    .customerId(trx.getCustomer().getId())
                    .transDate(trx.getTransDate())
                    .billDetail(trxDetailResponses)
                    .build();
        });
    }
}
