package com.enigma.wmb_api.dto.response;


import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ExportBillResponse {
    private Long billId;
    private String customerName;
    private Long totalAmount;

    public Date date;
}
