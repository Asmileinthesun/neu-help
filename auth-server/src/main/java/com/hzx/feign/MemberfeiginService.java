package com.hzx.feign;

import com.hzx.common.utils.R;
import com.hzx.vo.UserRegister;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient
public interface MemberfeiginService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegister userRegistVo);

}
