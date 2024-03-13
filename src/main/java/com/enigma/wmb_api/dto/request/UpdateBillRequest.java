package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBillRequest {
    private String orderId;
    private String transactionStatus;
}
