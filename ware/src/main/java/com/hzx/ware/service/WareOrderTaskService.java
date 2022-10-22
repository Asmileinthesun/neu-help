package com.hzx.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-02 00:46:52
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}

