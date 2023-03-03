package com.hzx;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
//import com.hzx.service.Msmservice;
//import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

//@Service
public class MsmserviceImpl {

//    @Override
    public Boolean send() {
        DefaultProfile profile = DefaultProfile.getProfile("default", "xxx", "xxx");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
//request.setProtocol(ProtocolType.HTTPS);
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        request.putQueryParameter("PhoneNumbers","xxx");
        request.putQueryParameter("SignName", "阿里云短信测试");//签名名称
        request.putQueryParameter("TemplateCode", "SMS_154950909");
        request.putQueryParameter("TemplateParam","{\"code\":\"1234\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }


}
