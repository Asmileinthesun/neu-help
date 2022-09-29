package com.hzx.product.web;

import com.hzx.product.entity.CategoryEntity;
import com.hzx.product.service.CategoryService;
import com.hzx.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

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

//    index/catalog.json
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> map=categoryService.getCatalogJson();
        return map;
    }
}
