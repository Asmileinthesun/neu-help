package com.hzx;

import com.aliyun.oss.*;
import com.hzx.product.entity.BrandEntity;
import com.hzx.product.service.BrandService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

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
