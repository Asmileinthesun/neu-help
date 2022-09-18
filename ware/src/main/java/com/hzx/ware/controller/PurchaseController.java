package com.hzx.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hzx.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hzx.ware.entity.PurchaseEntity;
import com.hzx.ware.service.PurchaseService;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.R;



/**
 * 采购信息
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-02 00:46:52
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
//    /ware/purchase/received
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }
///ware/purchase/merge
    @PostMapping("/merge")
    public R Merge(@RequestBody MergeVo mergeVo){
         purchaseService.mergepurchase(mergeVo);
        return R.ok();
    }
//    /ware/purchase/unreceive/list
@GetMapping("unreceive/list")
public R unreceivelist(@RequestParam Map<String, Object> params){
    PageUtils page = purchaseService.queryPageUnreceive(params);

    return R.ok().put("page", page);
}
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
