package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCustomerRequest {
    private String name;
    private String phoneNumber;
    private Boolean status;
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;

}
