package com.hzx.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.to.mq.OrderTo;
import com.hzx.common.to.mq.StockLockedTo;
import com.hzx.common.utils.PageUtils;
import com.hzx.ware.entity.WareSkuEntity;
import com.hzx.ware.vo.SkuHasStockVo;
import com.hzx.ware.vo.WareSkuLockVo;
import org.springframework.transaction.annotation.Transactional;

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

    void unlockStock(StockLockedTo to);

    //防止订单服务卡顿，导致订单状态消息一直改不了，库存消息优先到期。查订单状态新建状态，什么都不做就走了。
    //导致卡顿的订单，永远不能解锁库存
    @Transactional
    void unlockStock(OrderTo orderTo);
}

