package com.ulaiber.web.test;

import com.ulaiber.web.utils.BCrypt;
import org.junit.Test;

/**
 * 测试类
 * Created by daiqingwen on 2017/11/29.
 */
public class TestBCrypt {

    @Test
    public void encrypt(){
        String pwd = "987654";
        String hash = BCrypt.hashpw(pwd, BCrypt.gensalt());
        System.out.println(">>>>>>password:"+hash);
    }

    @Test
    public void decode(){
        String pwd = "123456";
        String hash = "6700e28167adc46e61422ddbabea87e9";
        System.out.println(">>>>>>decrypt before : "+BCrypt.checkpw(pwd, hash));
    }
}
