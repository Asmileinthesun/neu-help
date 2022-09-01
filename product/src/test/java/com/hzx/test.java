package com.hzx;

import com.hzx.product.entity.BrandEntity;
import com.hzx.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {
    @Autowired
    BrandService brandService;


    @Test
    public void  tese(){
        BrandEntity brand = new BrandEntity();
        brand.setName("huawei");
        brandService.save(brand);
        System.out.println("brand = " + brand);
    }
}
