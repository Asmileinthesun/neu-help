package com.hzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.product.entity.AttrAttrgroupRelationEntity;
import com.hzx.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:03:45
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBatch2(List<AttrGroupRelationVo> attrGroupRelationVo);
}

