package com.hzx.member.dao;

import com.hzx.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 21:02:21
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
