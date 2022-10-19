package com.hzx.product.service.impl;

import com.hzx.common.constant.ProductConstant;
import com.hzx.common.to.SkuReductionTo;
import com.hzx.common.to.SpuBoundsTo;
import com.hzx.common.to.es.SkuEsModel;
import com.hzx.common.utils.R;
import com.hzx.product.entity.*;
import com.hzx.product.feign.CouponFeignService;
import com.hzx.product.feign.SearchFeign;
import com.hzx.product.feign.WareFeignService;
import com.hzx.product.service.*;
import com.hzx.product.vo.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.Query;

import com.hzx.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

//@Accessors(chain = true)
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    AttrService attrService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeign searchFeign;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

//TODO
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuInfo) {
//1
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo,spuInfoEntity);
        spuInfoEntity.setCatalogId(spuInfo.getCatelogId());
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        //2
        List<String> decript = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        //3
        List<String> images = spuInfo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);
        //4
        List<BaseAttrs> baseAttrs = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(baseAttrs1 -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(baseAttrs1.getAttrId());
            String attrName = attrService.getById(baseAttrs1.getAttrId()).getAttrName();
            productAttrValueEntity.setAttrName(attrName);
            productAttrValueEntity.setAttrValue(baseAttrs1.getAttrValues());
            productAttrValueEntity.setQuickShow(baseAttrs1.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);
        //
        Bounds bounds = spuInfo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds,spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundsTo);
        if (r.getCode()!=0){
            log.error("远程保存spu积分信息失败");
        }

        //5
        List<Skus> skus = spuInfo.getSkus();
        if (skus!= null&&skus.size()>0) {
            skus.forEach(skus1 -> {
                String defaultImg="";
                for (Images image : skus1.getImages()) {
                    if(image.getDefaultImg()==1){
                        defaultImg=image.getImgUrl();
                    }
                }
                //5.1
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(skus1,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                //5.2
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> collect1 = skus1.getImages().stream().map(images1 -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(images1.getImgUrl());
                    skuImagesEntity.setDefaultImg(images1.getDefaultImg());
                    return skuImagesEntity;
                }).filter(skuImagesEntity -> !StringUtils.isEmpty(skuImagesEntity.getImgUrl())).collect(Collectors.toList());
                //TODO
                skuImagesService.saveBatch(collect1);
                //5.3
                List<Attr> attr = skus1.getAttr();
                List<SkuSaleAttrValueEntity> collect2 = attr.stream().map(attr1 -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr1, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(collect2);
                //5.4
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(skus1,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount()>0&&skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode()!=0){
                        log.error("远程保存sku信息优惠失败");
                    }
                }

            });
        }

    }


    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfo) {
        this.baseMapper.insert(spuInfo);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
//        status: 1
//        brandId: 1
//        catelogId: 225
//        key:
        if (!StringUtils.isEmpty(key)){
            wrapper.and(w->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        System.out.println("params = " + params);
        System.err.println("status = " + status);
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId = (String) params.get("brandId");
        System.err.println("brandId = " + brandId);
        if (!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);

    }

    @Override
    public void up(Long spuId) {
        //1
       List<SkuInfoEntity>skuInfoEntities= skuInfoService.getSkusBySpuId(spuId);
        List<Long> collect3 = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //
        List<ProductAttrValueEntity> attrlistforspu = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> collect1 = attrlistforspu.stream().map(attr -> attr.getAttrId()).collect(Collectors.toList());
       List<Long>list= attrService.selecrSearchAttrs(collect1);
       Set<Long> set=new HashSet<>(list);

        List<SkuEsModel.Attrs> collect2 = attrlistforspu.stream()
                .filter(item -> set.contains(item.getAttrId()))
                .map(item -> {
                    SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(item, attrs1);
                    return attrs1;
                }).collect(Collectors.toList());
        //
        Map<String, Object> map=null;
        try {
            R r = wareFeignService.getSkushasStock(collect3);
             map = (Map<String, Object>) r.get("data");
            System.err.println("map = " + map);
        }catch (Exception e){}
        //2
        Map<String, Object> finalMap = map;
        List<SkuEsModel> collect = skuInfoEntities.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity,skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());
            //TODO
            if (finalMap ==null){
                skuEsModel.setHasStock(true);
            }else {
                skuEsModel.setHasStock((Boolean) finalMap.get("hasStock"));
            }
            skuEsModel.setHotScore(0L);
            BrandEntity brand = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());
            CategoryEntity category = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(category.getName());
            //
            skuEsModel.setAttrs(collect2);
            return skuEsModel;
        }).collect(Collectors.toList());
        //TODO
        R r = searchFeign.productStatusUp(collect);
        if (r.getCode()==0){
            //TODO
            this.baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else {
            //TODO
        }

    }

    /**
     * 根据skuId查询spu的信息
     * @param skuId
     * @return
     */
    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {

        //先查询sku表里的数据
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);

        //获得spuId
        Long spuId = skuInfoEntity.getSpuId();

        //再通过spuId查询spuInfo信息表里的数据
        SpuInfoEntity spuInfoEntity = this.baseMapper.selectById(spuId);

        //查询品牌表的数据获取品牌名
//        BrandEntity brandEntity = brandService.getById(spuInfoEntity.getBrandId());
//        spuInfoEntity.setBrandName(brandEntity.getName());

        return spuInfoEntity;
    }

}