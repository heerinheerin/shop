package com.shop.controller;

import com.shop.constant.ItemCategory;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor

public class MainController {
    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        System.out.println(items.getNumber()+"!!!!!!!");
        System.out.println(items.getTotalPages() + "#########");
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);
        return "main";
    }

    @GetMapping(value = "/wangu")
    public String wangu(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getCategoryItemPage(ItemCategory.WANGU, pageable);
        System.out.println(items.getNumber()+"!!!!!!!");
        System.out.println(items.getTotalPages() + "#########");
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/wangu";
    }

    @GetMapping(value = "/role")
    public String role(ItemSearchDto itemSearchDto,Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getCategoryItemPage(ItemCategory.ROLE, pageable);
        System.out.println(items.getNumber()+"!!!!!!!");
        System.out.println(items.getTotalPages() + "#########");
        model.addAttribute("items", items);
       model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/role";
    }
    @GetMapping(value = "/bath")
    public String bath(ItemSearchDto itemSearchDto,Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getCategoryItemPage(ItemCategory.BATH, pageable);
        System.out.println(items.getNumber()+"!!!!!!!");
        System.out.println(items.getTotalPages() + "#########");
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/bath";
    }


}
