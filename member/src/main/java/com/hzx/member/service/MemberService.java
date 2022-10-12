package com.hzx.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.common.utils.PageUtils;
import com.hzx.member.entity.MemberEntity;
import com.hzx.member.vo.MemberLoginVo;
import com.hzx.member.vo.UserRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 21:02:21
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(UserRegistVo userRegistVo);
    void checkPhoneUnique(String phone);
    void checkUserNameUnique(String userName);

    MemberEntity login(MemberLoginVo memberLoginVo);
}

