package com.hzx.controller;

import com.hzx.interceptor.CartInterceptor;
import com.hzx.service.CartService;
import com.hzx.to.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-30 17:12
 **/

@Controller
public class CartController {

    @Resource
    private CartService cartService;


    /**
     * 去购物车页面的请求
     * 浏览器有一个cookie:user-key 标识用户的身份，一个月过期
     * 如果第一次使用jd的购物车功能，都会给一个临时的用户身份:
     * 浏览器以后保存，每次访问都会带上这个cookie；
     *
     * 登录：session有
     * 没登录：按照cookie里面带来user-key来做
     * 第一次，如果没有临时用户，自动创建一个临时用户
     *
     * @return
     */
    @GetMapping(value = "/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        //快速得到用户信息：id,user-key
         UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();

        System.out.println("userInfoTo = " + userInfoTo);
        return "cartList";
    }



}
