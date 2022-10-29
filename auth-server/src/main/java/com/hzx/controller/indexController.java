package com.hzx.controller;

import com.alibaba.fastjson.TypeReference;
import com.hzx.common.constant.AuthServerConstant;
import com.hzx.common.exception.BizCodeEnume;
import com.hzx.common.utils.R;
import com.hzx.common.vo.MemberVo;
import com.hzx.config.SendEmail;
import com.hzx.feign.MemberfeiginService;
import com.hzx.vo.UserLoginVo;
import com.hzx.vo.UserRegister;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hzx.common.constant.AuthServerConstant.LOGIN_USER;

@Controller
public class indexController {
    @Autowired
    MemberfeiginService memberfeiginService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @ResponseBody
    @GetMapping("/sendcode")
    public R sendemail(@RequestParam("email") String email, @RequestParam("length")int length){

        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + email);
        if (!StringUtils.isEmpty(s)){
            long l = Long.parseLong(s.split("_")[1]);
            if (System.currentTimeMillis()-l<60000){
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        SendEmail sendEmail=new SendEmail();
        //设置要发送的邮箱"2830768536@qq.com"
        sendEmail.setReceiveMailAccount(email);
        //创建10位发验证码
        Random random=new Random();
        String str="";
        for(int i=0;i<length;i++) {
            int n=random.nextInt(10);
            str+=n;
        }
        sendEmail.setInfo(str);
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+email,str+"_"+System.currentTimeMillis(),10, TimeUnit.MINUTES);
        try {
            sendEmail.Send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok();
    }
    @PostMapping("/regist")
    public String regist(@Valid UserRegister userRegister, BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> collect = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> fieldError.getDefaultMessage()));
//            model.addAttribute("errors",collect);
            redirectAttributes.addFlashAttribute("errors",collect);
            return "redirect:http://auth.gulimall.com/reg.html";

        }
        String code = userRegister.getCode();
        if (code.equals("1234")){
            R regist = memberfeiginService.regist(userRegister);
            if (regist.getCode() == 0) {
                return "redirect:http://auth.gulimall.com/login.html";
            }else {
                Map<String, String> collect = new HashMap<>();
                collect.put("msg",regist.getData("msg",new TypeReference<String>(){}));
                redirectAttributes.addFlashAttribute("errors",collect);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        }else {
            Map<String, String> collect = new HashMap<>();
            collect.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",collect);
            return "redirect:http://auth.gulimall.com/reg.html";
        }
//        return "redirect:/login.html";
    }
    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo, RedirectAttributes redirectAttributes,
                        HttpSession session){
        R login = memberfeiginService.login(userLoginVo);
        if (login.getCode()==0){
            MemberVo data = login.getData("data", new TypeReference<MemberVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:http://gulimall.com";
        }else {
            Map<String,String>errors=new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(LOGIN_USER);
        if (attribute == null) {
            return "login";
        }else {
            return "redirect:http://gulimall.com";
        }

    }
}
