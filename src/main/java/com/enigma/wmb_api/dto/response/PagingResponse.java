package com.enigma.wmb_api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingResponse {
    private Integer totalPage;
    private Long totalElemet;
    private Integer page;
    private Integer size;
    private boolean hasNext;
    private boolean hasPrevious;
}
