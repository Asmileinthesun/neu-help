package com.hzx.search.service.impl;

import com.hzx.search.comfig.ElasticsearchConfig;
import com.hzx.search.constant.EsCons;
import com.hzx.search.service.MallSearchService;
import com.hzx.search.vo.SearchParam;
import com.hzx.search.vo.SearchRes;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;

@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public SearchRes search(SearchParam searchParam) {
        SearchRes res=null;
        //1准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            //2
            SearchResponse search = client.search(searchRequest, ElasticsearchConfig.COMMON_OPTIONS);
            //3
           res= buildSearchResult(search);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    /**
     * 准备检索结果
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam Param) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        /**
         * 匹配
         */
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //must
        if (!StringUtils.isEmpty(Param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",Param.getKeyword()));
        }
        //filter
        if (Param.getCatalog3Id()!=null){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",Param.getCatalog3Id()));
        }

        if (Param.getBrandId()!=null&&Param.getBrandId().size()>0){
            boolQuery.filter(QueryBuilders.termQuery("brandId",Param.getBrandId()));
        }
        //
        if (Param.getAttrs()!=null&&Param.getAttrs().size()>0){

            for (String attr : Param.getAttrs()) {
                BoolQueryBuilder nestboolQueryBuilder = QueryBuilders.boolQuery();
                String[] str = attr.split("_");
                String attrId = str[0];
                String[] attrValues = str[1].split(":");
                nestboolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestboolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue",attrValues));
                //
                NestedQueryBuilder attrs = QueryBuilders.nestedQuery("attrs",nestboolQueryBuilder , ScoreMode.None);
                boolQuery.filter(attrs);
            }
        }

        if (!StringUtils.isEmpty(Param.getSkuPrice())){
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = Param.getSkuPrice().split("_");
            if (s.length==2){
                rangeQuery.gte(s[0]).lte(s[1]);
            }else if (s.length==1){
                if (Param.getSkuPrice().startsWith("_")){
                    rangeQuery.lte(s[0]);
                }else if (Param.getSkuPrice().endsWith("_")){
                    rangeQuery.gte(s[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }

        boolQuery.filter(QueryBuilders.termQuery("hasStock",Param.getHasStock()==1));

        builder.query(boolQuery);
        /**
         * 排序
         */
        if (!StringUtils.isEmpty(Param.getSort())){
            String sort = Param.getSort();
            String[] s = sort.split("_");
//            SortOrder order=s[1].equalsIgnoreCase("asc")?SortOrder.ASCENDING:SortOrder.DESCENDING;
            SortOrder order=s[1].equalsIgnoreCase("asc")?SortOrder.ASC:SortOrder.DESC;
            builder.sort(s[0],order);
        }
        builder.from((Param.getPageNum()-1)*EsCons.PRODUCT_PAGESIZE);
        builder.size(EsCons.PRODUCT_PAGESIZE);
        if (!StringUtils.isEmpty(Param.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            builder.highlighter(highlightBuilder);
        }
        /**
         * 聚合
         */
        //1
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(10);
        //1.1
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        builder.aggregation(brand_agg);
        //2
        TermsAggregationBuilder size = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        size.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        builder.aggregation(size);
        //3
        NestedAggregationBuilder nested = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(10));
        nested.subAggregation(attr_id_agg);
        builder.aggregation(nested);


        String s = builder.toString();
        System.out.println("s = " + s);
        SearchRequest  searchRequest= new SearchRequest(new String[]{EsCons.PRODUCT_INDEX}, builder);
        return searchRequest;
    }
    /**
     * 构建结果数据
     * @param search
     * @return
     */
    private SearchRes buildSearchResult(SearchResponse search) {
        return null;
    }


}
