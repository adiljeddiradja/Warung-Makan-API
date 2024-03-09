package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.NewMenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.request.UpdateMenuRequest;
import com.enigma.wmb_api.dto.response.MenuRespone;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface MenuService {
    MenuRespone create(NewMenuRequest request);

    MenuRespone getOneById(String id);

    Menu getById(String id);

    Page<MenuRespone> getAll(SearchMenuRequest request);

    MenuRespone update(UpdateMenuRequest request);

    Menu update(Menu menu);

    void deleteById(String id);


}
