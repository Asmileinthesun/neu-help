package com.hzx.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hzx.common.exception.BizCodeEnume;
import com.hzx.common.exception.NoStockException;
import com.hzx.ware.vo.SkuHasStockVo;
import com.hzx.ware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hzx.ware.entity.WareSkuEntity;
import com.hzx.ware.service.WareSkuService;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.R;



/**
 * 商品库存
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-02 00:46:52
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 锁定库存
     * @param vo
     * @return
     */
    @PostMapping(value = "/lock/order")
   public R orderLockStock(@RequestBody WareSkuLockVo vo){
        try{
            Boolean stock = wareSkuService.orderLockStock(vo);
            return R.ok();
        }catch (NoStockException e){
            return R.error(BizCodeEnume.NO_STOCK_EXCEPTION.getCode(),e.getMessage());
        }
    }

@PostMapping("/hasstock")
    public R getSkushasStock(@RequestBody List<Long> skuids){
       List<SkuHasStockVo>vo= wareSkuService.getSkuHasStock(skuids);
        return R.ok().put("data",vo);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
