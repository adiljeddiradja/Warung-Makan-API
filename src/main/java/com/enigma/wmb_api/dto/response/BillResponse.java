package com.enigma.wmb_api.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillResponse {
    private String id;
    private String customerId;
    private String tablesId;
    private Date transDate;
    private List<BillDetailResponse> billDetail;
    private PaymentResponse paymentResponse;
    private Long Amount;
}
