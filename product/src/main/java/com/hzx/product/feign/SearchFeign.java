package com.hzx.product.feign;

import com.hzx.common.to.es.SkuEsModel;
import com.hzx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient("guli-search")
public interface SearchFeign {
    @PostMapping("/search/save/")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList);
}
