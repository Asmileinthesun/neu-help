package com.hzx.product.feign.fallback;

import com.hzx.common.exception.BizCodeEnume;
import com.hzx.common.utils.R;
import com.hzx.product.feign.SeckillFeignService;
//import com.xunqi.common.exception.BizCodeEnum;
//import com.xunqi.common.utils.R;
//import com.xunqi.gulimall.product.feign.SeckillFeignService;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-13 14:45
 **/

@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSeckilInfo(Long skuId) {
        return R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),BizCodeEnume.TO_MANY_REQUEST.getMsg());
    }
}
