package com.hzx.product.service.impl;

import com.hzx.product.entity.SkuImagesEntity;
import com.hzx.product.entity.SpuInfoDescEntity;
import com.hzx.product.entity.SpuInfoEntity;
import com.hzx.product.service.*;
import com.hzx.product.vo.SkuItemVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.Query;

import com.hzx.product.dao.SkuInfoDao;
import com.hzx.product.entity.SkuInfoEntity;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    AttrGroupService attrGroupService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        /**
         * page: 1
         * max: 0
         * limit: 10
         * min: 0
         * brandId: 0
         * catelogId: 0
         * key:
         */
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and(w->{
                w.eq("sku_id",key).or().like("sku_name",key);
            });
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

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(catelogId)){
            wrapper.ge("price",min);
        }
        String max= (String) params.get("max");

        if (!StringUtils.isEmpty(catelogId)){
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal("0"))==1){
                    wrapper.le("price",max);
                }
            }catch (Exception e){

            }

        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
              wrapper
        );

        return new PageUtils(page);

    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        return this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));
    }

    @Override
    public SkuItemVo item(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();
        SkuInfoEntity infoEntity = getById(skuId);
        Long catalogId = infoEntity.getCatalogId();
        Long spuId = infoEntity.getSpuId();
        skuItemVo.setSkuInfoEntity(infoEntity);

        //
        List<SkuImagesEntity>images= skuImagesService.getImagesBySkuId(skuId);
        skuItemVo.setImages(images);
        //
      List<SkuItemVo.SkuItemSaleAttrVo>saleAttrVos=  skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
      skuItemVo.setSaleAttrVos(saleAttrVos);
        //

        SpuInfoDescEntity spuinfo = spuInfoDescService.getById(spuId);
        skuItemVo.setDesp(spuinfo);
        //
        List<SkuItemVo.SpuItemBaseAttrVo>groupattrs=attrGroupService.getAttrWithattrsByspuId(spuId,catalogId);
        skuItemVo.setGroupattrs(groupattrs);
return null;
    }

}