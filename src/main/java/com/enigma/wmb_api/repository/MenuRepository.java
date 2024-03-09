package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Menu;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository

public interface MenuRepository extends JpaRepository <Menu, String>, JpaSpecificationExecutor<Menu> {
    List<Menu>findAllByNameLike(String name);
}
