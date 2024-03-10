package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.BillDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BillDetailService {
    List<BillDetail> createBulk(List<BillDetail>billDetails);
}
