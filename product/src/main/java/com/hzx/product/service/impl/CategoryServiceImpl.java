package com.hzx.product.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hzx.product.service.CategoryBrandRelationService;
import com.hzx.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.Query;

import com.hzx.product.dao.CategoryDao;
import com.hzx.product.entity.CategoryEntity;
import com.hzx.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

//    private Map<String ,Object>cache=new HashMap<>();
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listwithtree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> collect = entities.stream().filter((categoryEntity) ->
                categoryEntity.getParentCid() == 0).map((menu)->{
                    menu.setChildren(getChildrens(menu,entities));
                    return menu;
        }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long attrGroupId1) {
        List<Long> list=new ArrayList<>();
        List<Long> parentPath = findParentPath(attrGroupId1, list);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }

    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());

    }

    @Override
    public List<CategoryEntity> getLevel1Category() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson(){

        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)){
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDb();
            String s = JSON.toJSONString(catalogJsonFromDb);
            stringRedisTemplate.opsForValue().set("catalogJson",s);
        }
        return JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
});
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {

//        Map<String, List<Catelog2Vo>> json = (Map<String, List<Catelog2Vo>>) cache.get("catalogJson");
//        if (cache.get("catalogJson")==null){
//            cache.put("catalogJson",parent_cid);
//        }
//        return json;
            /**
             * 优化
             */
            List<CategoryEntity> categoryEntities2 = baseMapper.selectList(null);

            List<CategoryEntity>categoryEntities=getParent_cid(categoryEntities2, 0L);
            Map<String, List<Catelog2Vo>> parent_cid = categoryEntities.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                        List<CategoryEntity> selectList = getParent_cid(categoryEntities2, v.getCatId());
                        List<Catelog2Vo> collect = null;
                        if (selectList != null) {
                            collect = selectList.stream().map(item -> {
                                Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());
                                List<CategoryEntity> categoryEntities1 = getParent_cid(categoryEntities2, item.getCatId());
                                if (categoryEntities1 != null) {
                                    List<Catelog2Vo.Catelog3Vo> collect1 = categoryEntities1.stream().map(l3 -> {
                                        Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(item.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                        return catelog3Vo;
                                    }).collect(Collectors.toList());
                                    catelog2Vo.setCatalog3List(collect1);

                                }
                                return catelog2Vo;
                            }).collect(Collectors.toList());
                        }
                        return collect;
                    }
            ));

            return parent_cid;

    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntities2,Long parent_cid) {
        List<CategoryEntity> collect = categoryEntities2.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        return  collect;
//        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", item.getCatId()));
    }

    private List<Long>findParentPath(Long attrGroupId1, List<Long> list){
        list.add(attrGroupId1);
        CategoryEntity id = this.getById(attrGroupId1);
        if (id.getParentCid()!=0) {
            findParentPath(id.getParentCid(),list);
        }
        return  list;
    }
    private List<CategoryEntity> getChildrens(CategoryEntity menu, List<CategoryEntity> entities) {
        List<CategoryEntity> list = entities.stream().filter(categoryEntity -> categoryEntity.getParentCid().equals(menu.getCatId()))
                .map(category -> {
                    category.setChildren(getChildrens(category, entities));
                    return category;
                })
                .sorted(Comparator.comparingInt(menu2 -> (menu2.getSort() == null ? 0 : menu2.getSort())))
                .collect(Collectors.toList());
        return list;
    }

}