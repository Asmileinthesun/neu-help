package com.hzx.search.controller;

import com.hzx.common.exception.BizCodeEnume;
import com.hzx.common.to.es.SkuEsModel;
import com.hzx.common.utils.R;
import com.hzx.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSavecontrol {

    @Autowired
    ProductSaveService productSaveService;
    @PostMapping("/")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){
        boolean b=true;
        try {
           b= productSaveService.productStatusUp(skuEsModelList);
        } catch (IOException e) {
            log.error("商品上架错误",e);
            return R.error(BizCodeEnume.PEODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PEODUCT_UP_EXCEPTION.getMsg());
        }
        if (!b)return R.ok();
        else return R.error(BizCodeEnume.PEODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PEODUCT_UP_EXCEPTION.getMsg());
    }

}
