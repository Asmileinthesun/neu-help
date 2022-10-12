package com.hzx.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.hzx.common.exception.BizCodeEnume;
import com.hzx.member.exception.PhoneException;
import com.hzx.member.exception.UsernameException;
import com.hzx.member.feign.CouponFeign;
import com.hzx.member.vo.MemberLoginVo;
import com.hzx.member.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hzx.member.entity.MemberEntity;
import com.hzx.member.service.MemberService;
import com.hzx.common.utils.PageUtils;
import com.hzx.common.utils.R;



/**
 * 会员
 *
 * @author hzx
 * @email sunlightcs@gmail.com
 * @date 2022-09-01 21:02:21
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    CouponFeign couponFeign;
    @PostMapping("/regist")
    public R regist(@RequestBody UserRegistVo userRegistVo){
        try {
            System.out.println("userRegistVo = " + userRegistVo);
            memberService.regist(userRegistVo);
        }catch (PhoneException e){
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UsernameException e){
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(),BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        }

        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo memberLoginVo){
        MemberEntity memberEntity= memberService.login(memberLoginVo);
        if (memberEntity != null) {
            return R.ok();
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }

    }
    @RequestMapping("test")
    public R test(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("111111111111111111111111111111111");
        R membercoupons = couponFeign.membercoupons();
        Object coupons = membercoupons.get("coupons");
        return R.ok().put("member",memberEntity).put("coupons",coupons);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
