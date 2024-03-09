package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.NewMenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.request.UpdateProductRequest;
import com.enigma.wmb_api.dto.response.MenuRespone;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.spesification.MenuSpesification;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuRespone create(NewMenuRequest request) {
        validationUtil.validate(request);
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        menuRepository.saveAndFlush(menu);
        return convertMenuToMenuResponse(menu);
    }

    @Override
    public MenuRespone getOneById(String id) {
        Menu menu = findByIdOrThrowNotFound(id);
        return convertMenuToMenuResponse(menu);
    }

    @Override
    public Menu getById(String id) {
        return findByIdOrThrowNotFound(id);

    }
    @Transactional(readOnly = true)
    @Override
    public Page<MenuRespone> getAll(SearchMenuRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Menu> specification = MenuSpesification.getSpecification(request);
        return menuRepository.findAll(specification, pageable).map(this::convertMenuToMenuResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuRespone update(UpdateProductRequest request) {
        Menu currentMenu = findByIdOrThrowNotFound(request.getId());
        currentMenu.setName(request.getName());
        currentMenu.setPrice(currentMenu.getPrice());
        currentMenu.setStock(currentMenu.getStock());
        menuRepository.saveAndFlush(currentMenu);
        return convertMenuToMenuResponse(currentMenu);
    }

    @Override
    public Menu update(Menu menu) {
        return menuRepository.saveAndFlush(menu);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Menu currentMenu = getById(id);
        menuRepository.delete(currentMenu);

    }

    private MenuRespone convertMenuToMenuResponse(Menu product) {
        return MenuRespone.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    private Menu findByIdOrThrowNotFound(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }
}
