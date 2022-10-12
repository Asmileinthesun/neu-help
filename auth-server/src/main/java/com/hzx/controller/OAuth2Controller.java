package com.hzx.controller;

import com.alibaba.fastjson.JSON;
import com.hzx.common.utils.HttpUtils;
import com.hzx.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class OAuth2Controller {
    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code")String code) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        HashMap<String,String> header = new HashMap<>();
        HashMap<String,String> query = new HashMap<>();
        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("client_id","41a819c96a853feea52776db53b512215d0b9329d24e2db7d20e6e6721e77a2e");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/gitee/success");
        map.put("client_secret","5546593c48b4e906bbf56f236a4e9b6d149c3bfbf907119d99adfe7d97c54fe9");
        HttpResponse post = HttpUtils.doPost("http://gitee.com", "/oauth/token", "post", header, query, map);

        if (post.getStatusLine().getStatusCode()==200){
            String string = EntityUtils.toString(post.getEntity());
            SocialUser socialUser = JSON.parseObject(string, SocialUser.class);


        }else {
            return "redirect:http://auth.gulimall.com/login.html";
        }
        return "redirect:http://gulimall.com";
    }
}
