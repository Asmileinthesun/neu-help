package com.hzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.product.entity.AttrGroupEntity;
import com.hzx.product.vo.AttrGroupWithAttrsVo;
import com.hzx.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:03:45
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage2(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrsVo> getAttrGroupWitharrsBycatelogId(Long catelogId);

    List<SkuItemVo.SpuItemBaseAttrVo> getAttrWithattrsByspuId(Long spuId, Long catalogId);
}

