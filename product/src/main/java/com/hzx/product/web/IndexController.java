package com.hzx.product.web;

import com.hzx.product.entity.CategoryEntity;
import com.hzx.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","index.html"})
    public String indexPage(Model model){
       List<CategoryEntity> list= categoryService.getLevel1Category();
       model.addAttribute("categorys",list);
        return "index";
    }


}
