package com.hzx.product.service.impl;

//import com.hzx.product.constant.ProductConstant;
import com.hzx.common.constant.ProductConstant;
import com.hzx.product.dao.AttrAttrgroupRelationDao;
import com.hzx.product.dao.AttrGroupDao;
import com.hzx.product.dao.CategoryDao;
import com.hzx.product.entity.AttrAttrgroupRelationEntity;
import com.hzx.product.entity.AttrGroupEntity;
import com.hzx.product.entity.CategoryEntity;
import com.hzx.product.vo.AttrGroupRelationVo;
import com.hzx.product.vo.AttrRespVo;
import com.hzx.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.Query;

import com.hzx.product.dao.AttrDao;
import com.hzx.product.entity.AttrEntity;
import com.hzx.product.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.save(attrEntity);
        if (attr.getAttrType()== ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()&&attr.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            relation.setAttrGroupId(attr.getAttrGroupId());
            relation.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(relation);
        }

    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type","base".equalsIgnoreCase(type)?
                        ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(catelogId!=0){
            wrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> list = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);


            if("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity id = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
                if (id != null&&id.getAttrGroupId()!=null) {
                    String attrGroupName = attrGroupDao.selectById(id.getAttrGroupId()).getAttrGroupName();
                    attrRespVo.setGroupName(attrGroupName);
                }
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(list);
        return pageUtils;
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attrGroupId = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        Collection<Long> collection = attrGroupId.stream().map((attr) -> attr.getAttrId()).collect(Collectors.toList());
        if (collection.size() == 0) {
            return null;
        }
        return this.listByIds(collection);
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] relationVo) {
//        attrAttrgroupRelationDao.delete(new QueryWrapper<>().eq("attr_id",).eq("attr_group_id",))
        List<AttrAttrgroupRelationEntity> collect = Arrays.asList(relationVo).stream().map((item) -> {
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relation);
            return relation;
        }).collect(Collectors.toList());
        attrAttrgroupRelationDao.deleteBatchRelation(collect);
    }

    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        List<AttrGroupEntity> groupEntityList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> list = groupEntityList.stream().map((item) -> item.getAttrGroupId()).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> attrGroupId = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", list));
        List<Long> collect = attrGroupId.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        QueryWrapper<AttrEntity> Wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (collect.size()>0 ) {
            Wrapper.notIn("attr_id", collect);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            Wrapper.and(w->w.eq("attr_id",key).or().like("attr_name",key));
        }
        IPage<AttrEntity> iPage = this.page(new Query<AttrEntity>().getPage(params), Wrapper);
        PageUtils pageUtils = new PageUtils(iPage);
        return pageUtils;
    }

}