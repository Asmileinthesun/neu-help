package com.hzx;

import com.hzx.product.entity.BrandEntity;
import com.hzx.product.service.BrandService;
import com.hzx.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class test {
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    public void  tese(){
        BrandEntity brand = new BrandEntity();
        brand.setName("huawei");
        brandService.save(brand);
        System.out.println("brand = " + brand);
    }
    @Autowired
    RedissonClient redissonClient;
    @Test
    public void  tese2w1(){
        System.out.println("redissonClient = " + redissonClient);
    }
    @Test
    public void  tese21(){
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set("hello","world"+ UUID.randomUUID());
        String hello = opsForValue.get("hello");
        System.out.println("hello = " + hello);
    }
    @Test
    public void  tese2(){
        Long[] path = categoryService.findCatelogPath(225L);
        log.info("111:{}", Arrays.asList(path));
    }

}
