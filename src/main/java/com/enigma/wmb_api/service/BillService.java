package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BillService {
    BillResponse create(BillRequest request);

    Page<BillResponse> getAll(SearchBillRequest request);

}
