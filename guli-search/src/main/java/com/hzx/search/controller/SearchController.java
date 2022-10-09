package com.hzx.search.controller;

import com.hzx.search.service.MallSearchService;
import com.hzx.search.vo.SearchParam;
import com.hzx.search.vo.SearchRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model){

      SearchRes result=  mallSearchService.search(searchParam);
      model.addAttribute("result",result);
        System.err.println("result = " + result);
        return "list";
    }
}
