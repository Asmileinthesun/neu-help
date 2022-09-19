package com.hzx.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hzx.product.entity.ProductAttrValueEntity;
import com.hzx.product.service.ProductAttrValueService;
import com.hzx.product.vo.AttrGroupRelationVo;
import com.hzx.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hzx.product.entity.AttrEntity;
import com.hzx.product.service.AttrService;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.R;



/**
 * 商品属性
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:27:36
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;
//    /product/attr/update/{spuId}
@PostMapping("/update/{spuId}")
public R updatespu(@PathVariable("spuId") Long spuId,
                   @RequestBody List<ProductAttrValueEntity> productAttrValueEntities){
     productAttrValueService.updateSpuAttr(spuId,productAttrValueEntities);
    return R.ok();
}
//    /product/attr/base/listforspu/{spuId}
@GetMapping("/base/listforspu/{spuId}")
public R listforspu(@PathVariable("spuId") Long spuId){
   List<ProductAttrValueEntity>page= productAttrValueService.baseAttrlistforspu(spuId);
    return R.ok().put("data", page);
}
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType")String type){
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,type);
        return R.ok().put("page", page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
