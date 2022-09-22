package com.hzx.product.feign;

import com.hzx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
    R getSkushasStock(@RequestBody List<Long> skuids);
}
