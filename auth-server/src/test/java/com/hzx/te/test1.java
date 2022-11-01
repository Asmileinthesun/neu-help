package com.hzx.te;


import com.hzx.common.utils.HttpUtils;
import com.hzx.config.SendEmail;
//import com.netflix.client.http.HttpResponse;
import com.hzx.config.SmsCom;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RunWith(value = SpringRunner.class)
@SpringBootTest
public class test1 {
    @Test
    public void sendemail(){
        SendEmail sendEmail=new SendEmail();
        //设置要发送的邮箱
        sendEmail.setReceiveMailAccount("2830768536@qq.com");
        //创建10位发验证码
        Random random=new Random();
        String str="";
        for(int i=0;i<4;i++) {
            int n=random.nextInt(10);
            str+=n;
        }
        sendEmail.setInfo(str);
        try {
            sendEmail.Send();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void phone(){
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "c5716c1ea71b407498fed097c177431e";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:1234");
        bodys.put("phone_number", "");
        bodys.put("template_id", "TPL_0000");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();

    }
    }

    @Autowired
    SmsCom smsCom;
    @Test
    public void phone2(){
        smsCom.phone("15702681331","1234");
    }
}
