package com.hzx.product.web;

import com.hzx.product.service.SkuInfoService;
import com.hzx.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public String SkuItem(@PathVariable("skuId") Long skuId){
        System.out.println("skuId = " + skuId);
      SkuItemVo skuItemVo= skuInfoService.item(skuId);
        return "item";
    }
}
