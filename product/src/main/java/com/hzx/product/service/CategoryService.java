package com.hzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.product.entity.CategoryEntity;
import com.hzx.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 17:03:45
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listwithtree();

    void removeMenuByIds(List<Long> asList);

    Long[] findCatelogPath(Long attrGroupId1);

    void updateDetail(CategoryEntity category);

    List<CategoryEntity> getLevel1Category();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}

