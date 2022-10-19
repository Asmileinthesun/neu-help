package com.hzx.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.ware.entity.WareSkuEntity;
import com.hzx.ware.vo.SkuHasStockVo;
import com.hzx.ware.vo.WareSkuLockVo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-02 00:46:52
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuids);

    Boolean orderLockStock(WareSkuLockVo vo) throws InvocationTargetException, IllegalAccessException;
}

