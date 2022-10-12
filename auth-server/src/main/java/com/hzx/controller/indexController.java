package com.hzx.controller;

import com.alibaba.fastjson.TypeReference;
import com.hzx.common.utils.R;
import com.hzx.feign.MemberfeiginService;
import com.hzx.vo.UserLoginVo;
import com.hzx.vo.UserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class indexController {
    @Autowired
    MemberfeiginService memberfeiginService;
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
    public String login(UserLoginVo userLoginVo,RedirectAttributes redirectAttributes){
        R login = memberfeiginService.login(userLoginVo);
        if (login.getCode()==0){
            return "redirect:http://gulimall.com";
        }else {
            Map<String,String>errors=new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }
}
