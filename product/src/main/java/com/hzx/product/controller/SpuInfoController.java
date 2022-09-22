package com.hzx.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.hzx.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hzx.product.entity.SpuInfoEntity;
import com.hzx.product.service.SpuInfoService;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.R;



/**
 * spu信息
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:27:36
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {


    @Autowired
    private SpuInfoService spuInfoService;

//    /product/spuinfo/{spuId}/up
@PostMapping("/{spuId}/up")
public R SpuUp(@PathVariable("spuId")Long spuId){
    spuInfoService.up(spuId);
    return R.ok();
}
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo spuInfo){
//		spuInfoService.save(spuInfo);
        spuInfoService.saveSpuInfo(spuInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
