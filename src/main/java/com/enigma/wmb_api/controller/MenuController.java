package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.NewMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.MenuRespone;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.MENU_API)
public class MenuController {
    private final MenuService menuService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuRespone>> createNewMenu(@RequestBody NewMenuRequest request) {
        MenuRespone newMenu = menuService.create(request);
        CommonResponse<MenuRespone> response = CommonResponse.<MenuRespone>builder()
                .statusCode(HttpStatus.CREATED.value())
                .messege(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(newMenu)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
