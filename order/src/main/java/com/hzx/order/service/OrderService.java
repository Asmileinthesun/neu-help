package com.hzx.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.order.entity.OrderEntity;
import com.hzx.order.vo.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-02 00:34:44
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);

    void closeOrder(OrderEntity orderEntity) throws InvocationTargetException, IllegalAccessException;

    OrderEntity getOrderByOrderSn(String orderSn);

    PayVo getOrderPay(String orderSn);

    PageUtils queryPageWithItem(Map<String, Object> params);

    String handlePayResult(PayAsyncVo asyncVo);

    String asyncNotify(String notifyData);
}

