package com.hzx.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {
    @GetMapping("/{skuId}.html")
    public String SkuItem(@PathVariable("skuId") Long skuId){
        System.out.println("skuId = " + skuId);
        return "item";
    }
}
