package com.hzx.feign;

import com.hzx.common.utils.R;
import com.hzx.vo.SocialUser;
import com.hzx.vo.UserLoginVo;
import com.hzx.vo.UserRegister;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberfeiginService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegister userRegistVo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo memberLoginVo);

    @PostMapping("/member/member/oauth2/login")
    R oauthlogin(@RequestBody SocialUser socialUser);
}
