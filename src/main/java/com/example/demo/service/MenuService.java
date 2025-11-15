package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.MenuDto;

public interface MenuService {
    List<MenuDto> getAllMenus(); // Lấy tất cả menu (có thể trả về dạng phẳng)
    // List<MenuDto> getMainMenuTree(); // Lấy menu chính dạng cây (nâng cao)
    MenuDto getMenuById(Integer id);
    MenuDto createMenu(MenuDto menuDto);
    MenuDto updateMenu(Integer id, MenuDto menuDto);
    void deleteMenu(Integer id);
}