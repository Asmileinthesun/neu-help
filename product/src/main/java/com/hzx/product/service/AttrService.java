package com.hzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.product.entity.AttrEntity;
import com.hzx.product.vo.AttrGroupRelationVo;
import com.hzx.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:03:45
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] relationVo);

    PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params);

    List<Long> selecrSearchAttrs(List<Long> collect1);
}

