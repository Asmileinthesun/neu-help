package com.hzx.search.vo;

import com.hzx.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchRes {
    private List<SkuEsModel> peoducts;
    /**
     * 分页信息
     */
    private Integer pageNum;
    private Long total;
    private Integer totalPage;
    private List<BrandVo> brandVos;
    private List<CatalogVo> catalogVos;
    private List<AttrVo> attrVos;

    @Data
    private static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    private static class CatalogVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    private static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}