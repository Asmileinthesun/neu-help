package com.hzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.product.entity.SkuSaleAttrValueEntity;
import com.hzx.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:03:45
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuItemVo.SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId);
}

