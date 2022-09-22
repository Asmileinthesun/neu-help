package search;

import com.alibaba.fastjson.JSON;
import com.hzx.search.comfig.ElasticsearchConfig;
import lombok.Data;
import net.minidev.json.JSONArray;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
//import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchConfig.class)
public class aa {
    @Autowired
    private RestHighLevelClient client;
    @Test
    public void contextLoads() {
        System.out.println("client = " + client);
    }
    @Test
    public void Search() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
//        searchSourceBuilder.from()
        System.out.println("searchSourceBuilder = " + searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, ElasticsearchConfig.COMMON_OPTIONS);
        //
        System.out.println("search = " + search.toString());
    }
    @Test
    public void index() throws IOException {
        IndexRequest indexRequest=new IndexRequest("users");
        indexRequest.id("1");
//        indexRequest.source("userName","zs","age",18,"gender","M");
        User user = new User();
        user.setUserName("zs");
        user.setAge(28);
        user.setGender("M");
        String string = JSON.toJSONString(user);
        indexRequest.source(string, XContentType.JSON);

        IndexResponse index = client.index(indexRequest, ElasticsearchConfig.COMMON_OPTIONS);
        System.out.println("index = " + index);
    }
    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }
}
