package com.example.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static RestTemplate template = new RestTemplate();;


    public static String get(String url){

        HttpEntity<String> entity = new HttpEntity<>(buildHeaders(url));
        return template.exchange(url, HttpMethod.GET, entity, String.class).getBody();

    }



    private static HttpHeaders buildHeaders(String refer){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","*/*");
        headers.add("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,es;q=0.7,sq;q=0.6,en-US;q=0.5");
        headers.add("Connection","keep-alive");
        headers.add("If-None-Match","W/\"ICiAJFmTZHE\"");
        headers.add("Referer","https://finance.sina.com.cn/realstock/company/sh000001/nc.shtml");
        headers.add("Sec-Fetch-Dest","script");
        headers.add("Sec-Fetch-Mode","no-cors");
        headers.add("Sec-Fetch-Site","cross-site");
        headers.add("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
        headers.add("sec-ch-ua","\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"");
        headers.add("sec-ch-ua-mobile","?0");
        headers.add("sec-ch-ua-platform","\"macOS\"");
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

}
