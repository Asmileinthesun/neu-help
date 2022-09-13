package com.hzx.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hzx.product.entity.AttrEntity;
import com.hzx.product.service.AttrAttrgroupRelationService;
import com.hzx.product.service.AttrService;
import com.hzx.product.service.CategoryService;
import com.hzx.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hzx.product.entity.AttrGroupEntity;
import com.hzx.product.service.AttrGroupService;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.R;



/**
 * 属性分组
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:27:36
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
//    /product/attrgroup/attr/relation
    @PostMapping("attr/relation")
    public R addrelation(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVo){
        attrAttrgroupRelationService.saveBatch2(attrGroupRelationVo);
        return R.ok();
    }
//    /product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrrelation(@PathVariable("attrgroupId")Long attrgroupId){
    List<AttrEntity>list= attrService.getRelationAttr(attrgroupId);
    return R.ok().put("data",list);
    }

//    /product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("{attrgroupId}/noattr/relation")
    public R noattrrelation(@PathVariable("attrgroupId")Long attrgroupId,@RequestParam Map<String, Object> params){
        PageUtils list= attrService.getNoRelationAttr(attrgroupId,params);
        return R.ok().put("page",list);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("/list/{catelogId}")
    public R list2(@RequestParam Map<String, Object> params,@PathVariable Long catelogId){
        PageUtils page = attrGroupService.queryPage2(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long attrGroupId1 = attrGroup.getCatelogId();
        Long[] path=categoryService.findCatelogPath(attrGroupId1);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    ///product/attrgroup/attr/relation/delete
    @PostMapping ("attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] relationVo){
        attrService.deleteRelation(relationVo);

        return R.ok();
    }
}
