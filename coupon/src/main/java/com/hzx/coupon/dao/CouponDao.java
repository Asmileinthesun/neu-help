package com.hzx.coupon.dao;

import com.hzx.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 20:26:20
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
