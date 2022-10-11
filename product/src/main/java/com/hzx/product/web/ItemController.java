package com.hzx.product.web;

import com.hzx.product.service.SkuInfoService;
import com.hzx.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public String SkuItem(@PathVariable("skuId") Long skuId, Model model){
        System.out.println("skuId = " + skuId);
      SkuItemVo skuItemVo= skuInfoService.item(skuId);
      model.addAttribute("item",skuItemVo);
        return "item";
    }
}
