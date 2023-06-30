package com.kim.spring.oauth2.uaa.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Kim Huang
 * @description
 * @date 2023-06-30
 */
public class CommonTest {


    @DisplayName("url编码")
    @Test
    public void urlEncode() throws UnsupportedEncodingException {
        String encode = URLEncoder.encode("http://www.baidu.com", "UTF-8");
        System.out.println(encode);
        String decode = URLDecoder.decode(encode, "UTF-8");
        System.out.println(decode);
    }

}
