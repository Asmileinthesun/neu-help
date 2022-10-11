package com.hzx;

import com.hzx.product.dao.AttrGroupDao;
import com.hzx.product.dao.SkuSaleAttrValueDao;
import com.hzx.product.entity.BrandEntity;
import com.hzx.product.service.BrandService;
import com.hzx.product.service.CategoryService;
import com.hzx.product.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
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
    @Autowired
    AttrGroupDao attrGroupDao;
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
    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Test
    public void  tese12(){
        List<SkuItemVo.SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(3L);
        System.out.println("saleAttrsBySpuId = " + saleAttrsBySpuId);
    }
    @Test
    public void  tese21w(){
        List<SkuItemVo.SpuItemBaseAttrVo> attrWithattrsByspuId = attrGroupDao.getAttrWithattrsByspuId(31L, 225L);
        System.out.println("attrWithattrsByspuId = " + attrWithattrsByspuId);
    }
}
