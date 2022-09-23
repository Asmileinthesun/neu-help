package com.hzx.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hzx.common.to.es.SkuEsModel;
import com.hzx.search.comfig.ElasticsearchConfig;
import com.hzx.search.constant.EsCons;
import com.hzx.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException {
        //1
        //2
        BulkRequest request = new BulkRequest();
        for (SkuEsModel model : skuEsModelList) {
            IndexRequest indexRequest = new IndexRequest(EsCons.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);
            request.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(request, ElasticsearchConfig.COMMON_OPTIONS);
        //TODO
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> item.getId()).collect(Collectors.toList());
        log.info("商品上架完成:{}",collect);

        return b;
    }
}
