package com.enigma.wmb_api.dto.response;

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
    private Date transDate;
    private List<BillDetailResponse> billDetail;
}
