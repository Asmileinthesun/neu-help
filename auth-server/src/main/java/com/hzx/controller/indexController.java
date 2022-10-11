package com.hzx.controller;

import com.hzx.vo.UserRegister;
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
    @PostMapping("/regist")
    public String regist(@Valid UserRegister userRegister, BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> collect = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> fieldError.getDefaultMessage()));
//            model.addAttribute("errors",collect);
            redirectAttributes.addFlashAttribute("errors",collect);
            return "redirect:http://auth.gulimall.com/reg.html";

        }
        return "redirect:/login.html";
    }
}
