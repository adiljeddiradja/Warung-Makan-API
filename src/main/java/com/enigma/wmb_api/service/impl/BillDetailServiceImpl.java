package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.repository.BillDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillDetailServiceImpl implements com.enigma.wmb_api.service.BillDetailService {
    private final BillDetailRepository billDetailRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BillDetail> createBulk(List<BillDetail> transactionDetails) {
        return billDetailRepository.saveAllAndFlush(transactionDetails);
    }
}
