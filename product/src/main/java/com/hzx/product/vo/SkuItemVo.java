package com.hzx.product.vo;

import com.hzx.product.entity.SkuImagesEntity;
import com.hzx.product.entity.SkuInfoEntity;
import com.hzx.product.entity.SpuInfoDescEntity;
import com.hzx.product.entity.SpuInfoEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class SkuItemVo {
    //1
    SkuInfoEntity skuInfoEntity;
    //2
    List<SkuImagesEntity> images;
    //3
    List<SkuItemSaleAttrVo>saleAttrVos;
    //4
    SpuInfoDescEntity desp;
    //5
    List<SpuItemBaseAttrVo>groupattrs;

    @Data
    public static class SkuItemSaleAttrVo{
        private Long attrId;
        private  String attrName;
        private List<String> attrValues;
    }
@ToString
    @Data
    public static class SpuItemBaseAttrVo{
        private String groupName;
        private List<Attr>attrs;

    }
}

