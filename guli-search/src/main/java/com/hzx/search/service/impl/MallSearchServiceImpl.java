package com.hzx.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hzx.common.to.es.SkuEsModel;
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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public SearchRes search(SearchParam searchParam) {
        SearchRes res=null;
        //1准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);
//        System.out.println("searchRequest = " + searchRequest);
        try {
            //2
            SearchResponse search = client.search(searchRequest, ElasticsearchConfig.COMMON_OPTIONS);
            //3
           res= buildSearchResult(search,searchParam);
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

        if (Param.getHasStock()!=null){
            boolQuery.filter(QueryBuilders.termQuery("hasStock",Param.getHasStock()==1));
        }

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
//        System.out.println("s = " + s);
        SearchRequest  searchRequest= new SearchRequest(new String[]{EsCons.PRODUCT_INDEX}, builder);
        return searchRequest;
    }
    /**
     * 构建结果数据
     * @param search
     * @return
     */
    private SearchRes buildSearchResult(SearchResponse search,SearchParam Param) {
        SearchRes res = new SearchRes();
        SearchHits hits = search.getHits();
        List<SkuEsModel>esModels=new ArrayList<>();
        if (hits.getHits()!=null&&hits.getHits().length>0){
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString,SkuEsModel.class);
                if (!StringUtils.isEmpty(Param.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(string);
                }
                esModels.add(skuEsModel);
            }
        }
        res.setProducts(esModels);
        //
        Terms catalog_agg = search.getAggregations().get("catalog_agg");
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        List<SearchRes.CatalogVo>catalogVos=new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            SearchRes.CatalogVo catalogVo = new SearchRes.CatalogVo();
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));

            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String keyAsString1 = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(keyAsString1);
            catalogVos.add(catalogVo);
        }
        res.setCatalogVos(catalogVos);

        List<SearchRes.AttrVo>attrVos=new ArrayList<>();
        ParsedNested attr_agg = search.getAggregations().get("attr_agg");
        Terms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchRes.AttrVo attrVo = new SearchRes.AttrVo();
            long value = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(value);
            String attr_name_agg = ((Terms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attr_name_agg);
            List<String> valueAgg = ((Terms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList());
            attrVo.setAttrValue(valueAgg);
            attrVos.add(attrVo);
        }
        res.setAttrVos(attrVos);

        List<SearchRes.BrandVo>brandVos=new ArrayList<>();
        Terms brand_agg = search.getAggregations().get("brand_agg");
        List<? extends Terms.Bucket> buckets1 = brand_agg.getBuckets();
        for (Terms.Bucket bucket : buckets1) {
            SearchRes.BrandVo brandVo = new SearchRes.BrandVo();
            long value = bucket.getKeyAsNumber().longValue();
            Terms brand_img_agg = bucket.getAggregations().get("brand_img_agg");
            String keyAsString = brand_img_agg.getBuckets().get(0).getKeyAsString();
            Terms brand_name_agg = bucket.getAggregations().get("brand_name_agg");
            String keyAsString1 = brand_name_agg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(value);
            brandVo.setBrandImg(keyAsString);
            brandVo.setBrandName(keyAsString1);
            brandVos.add(brandVo);
        }
        res.setBrandVos(brandVos);

        //
        res.setPageNum(Param.getPageNum());
        res.setTotal(hits.getTotalHits().value);
        res.setTotalPage((int)(hits.getTotalHits().value+1)/EsCons.PRODUCT_PAGESIZE);
        return res;
    }


}
