package com.hzx.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.hzx.common.valid.AddGroup;
import com.hzx.common.valid.UpdateStatus;
import com.hzx.common.valid.updateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hzx.product.entity.BrandEntity;
import com.hzx.product.service.BrandService;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.R;


/**
 * 品牌
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:27:36
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class})
                      @RequestBody BrandEntity brand/*,BindingResult result*/){
//        if(result.hasErrors()){
//            Map<String ,String >map=new HashMap<>();
//            result.getFieldErrors().forEach((item)->{
//                String defaultMessage = item.getDefaultMessage();
//                String field = item.getField();
//                map.put(field,defaultMessage);
//            });
//           return R.error(400, "提交的数据不合法").put("data",map);
//        }else {
//
//        }
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(value = {updateGroup.class})@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }
    @RequestMapping("/update/status")
    public R updateStatus(@Validated(value = {UpdateStatus.class})@RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
