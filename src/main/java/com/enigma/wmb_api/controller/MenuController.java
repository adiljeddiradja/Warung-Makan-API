package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.NewMenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.request.UpdateMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.MenuRespone;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<MenuRespone>> getMenuById(@PathVariable String id) {
        MenuRespone menu = menuService.getOneById(id);
        CommonResponse<MenuRespone> response = CommonResponse.<MenuRespone>builder()
                .statusCode(HttpStatus.OK.value())
                .messege(ResponseMessage.SUCCESS_GET_DATA)
                .data(menu)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<MenuRespone>>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();
        Page<MenuRespone> menus = menuService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(menus.getTotalPages())
                .totalElemet(menus.getTotalElements())
                .hasNext(menus.hasNext())
                .hasPrevious(menus.hasPrevious())
                .page(menus.getPageable().getPageNumber())
                .size(menus.getPageable().getPageSize())
                .build();

        CommonResponse<List<MenuRespone>> response = CommonResponse.<List<MenuRespone>>builder()
                .statusCode(HttpStatus.OK.value())
                .messege("Success get all data!")
                .data(menus.getContent())
                .pagging(pagingResponse)
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuRespone>> UpdateMenu(@RequestBody UpdateMenuRequest request) {
        MenuRespone updatedMenu = menuService.update(request);
        CommonResponse<MenuRespone> response = CommonResponse.<MenuRespone>builder()
                .statusCode(HttpStatus.OK.value())
                .messege(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(updatedMenu)
                .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<?>> deleteById(@PathVariable String id) {
        menuService.deleteById(id);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .messege(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }
}
