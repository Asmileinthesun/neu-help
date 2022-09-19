package com.hzx.ware.service.impl;

import com.hzx.common.constant.WareConstant;
import com.hzx.ware.entity.PurchaseDetailEntity;
import com.hzx.ware.service.PurchaseDetailService;
import com.hzx.ware.service.WareSkuService;
import com.hzx.ware.vo.MergeVo;
import com.hzx.ware.vo.PurchaseFinishVo;
import com.hzx.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.Query;

import com.hzx.ware.dao.PurchaseDao;
import com.hzx.ware.entity.PurchaseEntity;
import com.hzx.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper.eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergepurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity detail = new PurchaseDetailEntity();
            detail.setId(i);
            detail.setPurchaseId(finalPurchaseId);
            detail.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGEND.getCode());
            return detail;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setId(purchaseId);
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);
    }

    @Override
    public void received(List<Long> ids) {
        //1
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity purchaseEntity = this.getById(id);
            return purchaseEntity;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGEND.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        //2
        this.updateBatchById(collect);
        //3
        collect.forEach(item->{
            List<PurchaseDetailEntity> list=purchaseDetailService.listDetailByPurChaseId(item.getId());
            List<PurchaseDetailEntity> collect1 = list.stream().map(purchaseDetailEntity -> {
                PurchaseDetailEntity purchaseDetailEntity1 = new PurchaseDetailEntity();
                purchaseDetailEntity1.setId(purchaseDetailEntity.getId());
                purchaseDetailEntity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return purchaseDetailEntity1;
            }).collect(Collectors.toList());

            purchaseDetailService.updateBatchById(collect1);

        });
    }

    @Override
    public void done(PurchaseFinishVo purchaseItemDoneVo) {
        //1
        Long id = purchaseItemDoneVo.getId();
        //2
        Boolean flag=true;
        List<PurchaseItemDoneVo> items = purchaseItemDoneVo.getItems();
        List<PurchaseDetailEntity> updates= new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (item.getStatus()==WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                flag=false;
                purchaseDetailEntity.setStatus(item.getStatus());
            }else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //3
                PurchaseDetailEntity byId = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(byId.getSkuId(),byId.getWareId(),byId.getSkuNum());
            }
            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(updates);
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setId(id);
        purchase.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);


    }

}