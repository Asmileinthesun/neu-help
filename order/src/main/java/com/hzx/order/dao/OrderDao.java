package com.hzx.order.dao;

import com.hzx.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-02 00:34:44
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
