package com.hzx.search.vo;

import com.hzx.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchRes {
    private List<SkuEsModel> products;
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
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}