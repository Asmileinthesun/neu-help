package com.hzx.service;

//import com.xunqi.gulimall.cart.vo.CartItemVo;
//import com.xunqi.gulimall.cart.vo.CartVo;


import com.hzx.vo.CartItemVo;

import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-30 17:06
 **/
public interface CartService {

    CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItemVo getCartItem(Long skuId);
}
