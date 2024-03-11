package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.request.SearchTablesRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Tables;
import com.enigma.wmb_api.service.TablesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.TABLES_API)
public class TablesController {
    private final TablesService tablesService;
    @PostMapping
    public ResponseEntity<Tables>  createNewTables(@RequestBody Tables tables){
        Tables newTables = tablesService.create(tables);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newTables);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Tables>getTablesById (@PathVariable String id){
        Tables tables= tablesService.getByid(id);
        return ResponseEntity.ok(tables);
    }
    @GetMapping
    public ResponseEntity<CommonResponse<List<Tables>>> getAllTables(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchTablesRequest request = SearchTablesRequest.builder()
                .name(name)
                .direction(direction)
                .size(size)
                .page(page)
                .sortBy(sortBy)
                .build();

        Page<Tables> tables = tablesService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(tables.getTotalPages())
                .totalElemet(tables.getTotalElements())
                .page(tables.getPageable().getPageNumber() + 1)
                .size(tables.getPageable().getPageSize())
                .hasNext(tables.hasNext())
                .hasPrevious(tables.hasPrevious())
                .build();

        CommonResponse<List<Tables>> response = CommonResponse.<List<Tables>>builder()
                .statusCode(HttpStatus.OK.value())
                .messege("success get all data")
                .data(tables.getContent())
                .pagging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

}
