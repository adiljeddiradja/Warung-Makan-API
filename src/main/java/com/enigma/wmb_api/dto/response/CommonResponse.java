package com.enigma.wmb_api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    private Integer statusCode;
    private String messege;
    private T data;
    private PagingResponse pagging;
}
