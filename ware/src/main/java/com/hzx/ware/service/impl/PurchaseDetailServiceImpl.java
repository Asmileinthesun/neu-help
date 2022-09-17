package com.hzx.ware.service.impl;

import com.hzx.ware.entity.WareInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.Query;

import com.hzx.ware.dao.PurchaseDetailDao;
import com.hzx.ware.entity.PurchaseDetailEntity;
import com.hzx.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         *    status: 0,//状态
         *    wareId: 1,//仓库id
         */
        String key= (String) params.get("key");
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)){
            wrapper.and(wareInfoEntityQueryWrapper -> {
                wareInfoEntityQueryWrapper.eq("purchase_id",key).or().eq("sku_id",key);
            });
        }
        String status= (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        String wareId= (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}