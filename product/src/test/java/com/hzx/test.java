package com.hzx;

import com.hzx.product.entity.BrandEntity;
import com.hzx.product.service.BrandService;
import com.hzx.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
@Slf4j
@SpringBootTest
public class test {
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Test
    public void  tese(){
        BrandEntity brand = new BrandEntity();
        brand.setName("huawei");
        brandService.save(brand);
        System.out.println("brand = " + brand);
    }
    @Test
    public void  tese2(){
        Long[] path = categoryService.findCatelogPath(225L);
        log.info("111:{}", Arrays.asList(path));
    }

}
