package com.hzx.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.hzx.common.utils.HttpUtils;
import com.hzx.member.dao.MemberLevelDao;
import com.hzx.member.entity.MemberLevelEntity;
import com.hzx.member.exception.PhoneException;
import com.hzx.member.exception.UsernameException;
import com.hzx.member.vo.MemberLoginVo;
import com.hzx.member.vo.SociUserinfo;
import com.hzx.member.vo.SocialUser;
import com.hzx.member.vo.UserRegistVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.Query;

import com.hzx.member.dao.MemberDao;
import com.hzx.member.entity.MemberEntity;
import com.hzx.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(UserRegistVo userRegistVo) {
        MemberEntity memberEntity = new MemberEntity();

        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(levelEntity.getId());

        //设置其它的默认信息
        //检查用户名和手机号是否唯一。感知异常，异常机制
        checkPhoneUnique(userRegistVo.getPhone());
        checkUserNameUnique(userRegistVo.getUserName());

        memberEntity.setNickname(userRegistVo.getUserName());
        memberEntity.setUsername(userRegistVo.getUserName());
        //密码进行MD5加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(userRegistVo.getPassword());
        memberEntity.setPassword(encode);
        memberEntity.setMobile(userRegistVo.getPhone());
        memberEntity.setGender(0);
        memberEntity.setCreateTime(new Date());
        System.err.println("true =创建成功 " + true);
        this.baseMapper.insert(memberEntity);
    }


    @Override
    public void checkPhoneUnique(String phone) throws PhoneException {

        Integer phoneCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));

        if (phoneCount > 0) {
            throw new PhoneException();
        }

    }

    @Override
    public void checkUserNameUnique(String userName) throws UsernameException {

        Integer usernameCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));

        if (usernameCount > 0) {
            throw new UsernameException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo memberLoginVo) {
        String loginacct = memberLoginVo.getLoginacct();
        String password = memberLoginVo.getPassword();
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct)
                .or().eq("mobile", loginacct));
        if (memberEntity == null) {
            return null;
        } else {
            String password1 = memberEntity.getPassword();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matches = bCryptPasswordEncoder.matches(password, password1);
            if (matches) return memberEntity;

        }
        return null;
    }

    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {
            //            https://gitee.com/api/v5/user?access_token=c92c014432552f232903002eb5a4d5d7
            HashMap<String,String> map2 = new HashMap<>();
            map2.put("access_token",socialUser.getAccess_token());
            HttpResponse get = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get",
                    new HashMap<>(), map2);
            if (get.getStatusLine().getStatusCode()==200){
                String string1 = EntityUtils.toString(get.getEntity());
                SociUserinfo sociUserinfo = JSON.parseObject(string1, SociUserinfo.class);
                String userId = String.valueOf(sociUserinfo.getId());
                MemberEntity social_uid = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", userId));
                if (social_uid !=null) {
                    MemberEntity memberEntity = new MemberEntity();
                    memberEntity.setId(social_uid.getId());
                    memberEntity.setAccessToken(socialUser.getAccess_token());
                    memberEntity.setExpiresIn(socialUser.getExpires_in());
                    this.baseMapper.updateById(memberEntity);
                    social_uid.setAccessToken(socialUser.getAccess_token());
                    social_uid.setExpiresIn(socialUser.getExpires_in());
                    return  social_uid;
                }else {
                    MemberEntity regist = new MemberEntity();
                    //TODO
                    regist.setNickname(sociUserinfo.getName());
                    regist.setSocialUid(String.valueOf(sociUserinfo.getId()));
                    regist.setAccessToken(socialUser.getAccess_token());
                    regist.setExpiresIn(socialUser.getExpires_in());
                    this.baseMapper.insert(regist);
                    return regist;
                }
            }else {
                return null;
            }


    }
}