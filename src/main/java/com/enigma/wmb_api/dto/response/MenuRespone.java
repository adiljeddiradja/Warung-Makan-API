package com.enigma.wmb_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRespone {
    private String id;
    private String name;
    private Long price;
    private Integer stock;

}
