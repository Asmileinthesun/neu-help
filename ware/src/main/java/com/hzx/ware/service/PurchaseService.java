package com.hzx.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.ware.entity.PurchaseEntity;
import com.hzx.ware.vo.MergeVo;
import com.hzx.ware.vo.PurchaseFinishVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-02 00:46:52
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergepurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseFinishVo purchaseItemDoneVo);
}

