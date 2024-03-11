package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.BILL_API)
public class BillController {
    private final BillService billService;
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<BillResponse>> createNewBill(@RequestBody BillRequest request) {
        BillResponse bill = billService.create(request);
        CommonResponse<BillResponse> response = CommonResponse.<BillResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .messege(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(bill)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<BillResponse>>>getAllBills(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size
    ){
        SearchBillRequest request = SearchBillRequest.builder()
                .page(page)
                .size(size)
                .build();

        Page<BillResponse> bills =billService.getAll(request);
        PagingResponse paging = PagingResponse.builder()
                .page(page)
                .size(size)
                .hasPrevious(bills.hasPrevious())
                .hasNext(bills.hasNext())
                .totalPage(bills.getTotalPages())
                .totalElemet(bills.getTotalElements())
                .build();
        CommonResponse<List<BillResponse>>response= CommonResponse.<List<BillResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .messege(ResponseMessage.SUCCESS_GET_DATA)
                .pagging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

}
