package com.hzx.te;


import com.hzx.config.SendEmail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
