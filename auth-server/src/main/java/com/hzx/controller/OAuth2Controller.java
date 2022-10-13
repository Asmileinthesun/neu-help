package com.hzx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hzx.common.constant.AuthServerConstant;
import com.hzx.common.utils.HttpUtils;
import com.hzx.common.utils.R;
import com.hzx.feign.MemberfeiginService;
import com.hzx.common.vo.MemberVo;
import com.hzx.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
@Slf4j
@Controller
public class OAuth2Controller {
    @Autowired
    MemberfeiginService memberfeiginService;
    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code")String code, HttpSession session, HttpServletResponse servletResponse) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("client_id","41a819c96a853feea52776db53b512215d0b9329d24e2db7d20e6e6721e77a2e");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/gitee/success");
        map.put("client_secret","5546593c48b4e906bbf56f236a4e9b6d149c3bfbf907119d99adfe7d97c54fe9");
        HttpResponse post = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), map, new HashMap<>());
        System.out.println("post.getStatusLine().getStatusCode() = " + post.getStatusLine().getStatusCode());
        if (post.getStatusLine().getStatusCode()==200){
            String string = EntityUtils.toString(post.getEntity());
            SocialUser socialUser = JSON.parseObject(string, SocialUser.class);
            //知道了哪个社交用户
            //1）、当前用户如果是第一次进网站，自动注册进来（为当前社交用户生成一个会员信息，以后这个社交账号就对应指定的会员）
            //登录或者注册这个社交用户
            System.out.println(socialUser.getAccess_token());
            //调用远程服务
            R oauthlogin = memberfeiginService.oauthlogin(socialUser);
            if (oauthlogin.getCode()==0) {
                MemberVo data = oauthlogin.getData("data", new TypeReference<MemberVo>() {});
                log.info("登陆成功{}",data.toString());
                session.setAttribute(AuthServerConstant.LOGIN_USER,data);
//                System.out.println("data = " + session.getAttribute("loginUser"));
//                System.out.println();
//                servletResponse.addCookie(new Cookie("JSESSIONID","data").setDomain());
                return "redirect:http://gulimall.com";
            }else {
                return  "redirect:http://auth.gulimall.com/login.html";
            }
        }else {
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}
