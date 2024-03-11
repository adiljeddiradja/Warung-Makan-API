package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchTablesRequest {
    private Boolean status;
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String name;
}
