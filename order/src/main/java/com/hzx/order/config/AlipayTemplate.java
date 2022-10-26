package com.hzx.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
//import com.xunqi.gulimall.order.vo.PayVo;
import com.hzx.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public String app_id="2021000117607316";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public String merchant_private_key="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDYhjXYyCFV9pkmBgP3uSrZOJGVZi3E7amt1sONhz8JLJQiKVxQ6MLiqg2fYfW2BaonFYG07RUYXBDTySp6RbVjeUrs42WGnSb7ECok8D/zQmZ+spgiSoR+riQFutf3cBSUdfySYehmaUNxK/bczO2bgA4c82E6umomJ4OlTGnk11xjks5UADtKvjUZ/WfwR2gZu9pEtwkZa3uD9jdgdJhwOuc6MTkDJ0wC2aUr4Xjl6j2AyGOyjZWOB9BbR42OoT5rs8S80F1EL99t0dHL/rGVoBvLO2HapBNARrtkcZh19ldbxEjNq67ftJjk18mmWOKxcgje9DFK29q1u3OXW83VAgMBAAECggEAeBM1mVqi4gE3PRbo7GALmxr74VYg26zRBE6tc7clBflqzOcTAuuR6BFWLKUulhreGX6bx7ISmMe1rT1KCTTPOrLxEh2pF9Xe4Cw+su1pCYYxQOHZ7GpWv8niwCq1ycqmHw6vWp5hOyn09LXk2N3shuek6/ksWl02sKIJJ4oQz455QhrHT4gyvMuXatQ72PDlmWSOkfWnIcvlOQuRfrJaliW6BZM+qRtcT1fF3cTCExi2uuMjhKKhuHGSlFEQM9FI7om+nNYOi6KDZvUD2z2LfiMhhkeFD5xKNvNt9C1nq3DwRJzL9hgCFAOIFI0ksSw8lbnUIjy70igHbLUcAYBhAQKBgQD7jZesAccYB279PNKnw1nnSwhZ/dSaGjiUXNM/SxGmgeXELKjsskiI+upJzAdykz+LNvhK6QPeaVcgCPrdLfXrAj8e6pAFTcRyLywQU158Qi3t5spRVx/nbT9xA72nvVmEtI3ZOYb9KnZykwHX2l5BrEAihuShbXwLjxZ89TutKQKBgQDcWhglAn2k1XAY4d/tOCoXE/DPhBuQssQuZsArO9jDxsW5UPJgSiZ1MSedQ/CQ47/fD8I+8CaptdNhG47BliKkgF8V6Ywq3s0lKYNctv7xOox/Twyf4BctNdPO3EF3KtSj190dyznhdv4M57Dp3P2ExmAYEqGF9HNocnXLByGEzQKBgBH9g15i1ilKxs5Hnk0vNGvNOvCsrZrDy04ZAWaiaMW+vn70LrSW34ckYsMaW2H/U+kR6ln5JjSu1fNNkRtmYRMImxkL73bho+4Cn6UfQT+jwAMmoUuF7psvff0IJnUrno5WF1r0uUm70rLI/rXAQUNzHTca7Jj66octGY+gPj9ZAoGAGSBI2oeDLjWMV7oB37nkysbiUbddsU1BI41h4kwrtn9G8o+xY5tw8TIOs1cY6lyd92CDpMubjQSHuu36hEaLYriknwoHiqfnOpKWQQ842Ob1OFsaNGzIF8aohVEGQgFnR9uIgio3SmdYJr6QpMonOmmocXZGZtDuC/3pQqJohw0CgYBRxTjdh/lhTH2+jZYivPsuiHrdvmj+P6PyvxMVHD2RGTtcfpKk4LvI9PyVjOCvqe5gzZEZl8XS8q6UwG80ezfBc6KKbIv6rEpj/CgEC1YXW864e3gaTUPexZhGbCyHbWCMWcV1yKHobTQH6cU+2q4IAkcBEJ4CGVLmzasS/4k+jw==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public String alipay_public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApFj496KuHqIZATuK+g3FvlHfFQplY+XEizJZ/3q5MCDuTjKGtdip88KVP8D1lZvIpKL+t12MmvOGiFrhcuZaB3sgBBQL2QyM6gZ2MlEPwZsMYq2ZXbctgKR1S+hVMm0P/ds9fIHrGzHpRbWBi+QTlLYmJoM4yC5LiPe3wZI2v5j7U08k0Y8U13zB7m+8RkQ5zRB40mTC2PtTOhNnOXx4sVyDh9+ISz07IR0SoWt6updHjbV+gR4gJ4PYa2Y2lCR1PCyLS9R3nZqea4k6uHlh4zl0tAozAW5jpp6NYv0k7B27iG4hskvNefIsJnjbql1rjSvpvtvmBEK6qNtTl02MyQIDAQAB";

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    public String notify_url="http://gulimall.com";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    public String return_url="http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type="RSA2";

    // 字符编码格式
    private  String charset="utf-8";

    //订单超时时间
    private String timeout = "1m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    public String gatewayUrl="https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
