package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.request.UpdateBillRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.service.BillService;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.BILL_API)
public class BillController {
    private final BillService billService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<BillResponse>> createNewTransaction(@RequestBody BillRequest request) {
        BillResponse bill = billService.create(request);
        CommonResponse<BillResponse> response = CommonResponse.<BillResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .messege(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(bill)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<BillResponse>>> getAllTransaction(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        SearchBillRequest request = SearchBillRequest.builder()
                .page(page)
                .size(size)
                .build();
        Page<BillResponse> bill = billService.getAll(request);
        PagingResponse paging = PagingResponse.builder()
                .page(bill.getPageable().getPageNumber() + 1)
                .size(bill.getPageable().getPageSize())
                .totalPage(bill.getTotalPages())
                .totalElemet(bill.getTotalElements())
                .hasNext(bill.hasNext())
                .hasPrevious(bill.hasPrevious())
                .build();
        CommonResponse<List<BillResponse>> response = CommonResponse.<List<BillResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .messege(ResponseMessage.SUCCESS_GET_DATA)
                .data(bill.getContent())
                .pagging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/status")
    public ResponseEntity<CommonResponse<?>> updateStatus(@RequestBody Map<String, Object> request) {
        UpdateBillRequest updateTransactionStatusRequest = UpdateBillRequest.builder()
                .orderId(request.get("order_id").toString())
                .transactionStatus(request.get("transaction_status").toString())
                .build();
        billService.updateStatus(updateTransactionStatusRequest);
        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .messege(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build());
    }
    @GetMapping("/export-csv")
    public void exportToCsv(HttpServletResponse response,
                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                            @RequestParam(name = "size", defaultValue = "10") Integer size) throws IOException {
        SearchBillRequest request = SearchBillRequest.builder()
                .page(page)
                .size(size)
                .build();
        Page<BillResponse> bill = billService.getAll(request);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=bills.csv");


        try (CSVWriter csvWriter = new CSVWriter(response.getWriter())) {

            csvWriter.writeNext(new String[]{"Bill ID", "Amount", "Date"});

            for (BillResponse billResponse : bill.getContent()) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(billResponse.getId()),
                        String.valueOf(billResponse.getAmount()),
                        billResponse.getTransDate().toString()
                });
            }
        }
    }
}
