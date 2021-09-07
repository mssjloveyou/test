package com.example.util;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;

public class HttpUtil {

    private static RestTemplate template = new RestTemplate(new SimpleClientHttpRequestFactory() {{
        //setProxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("cn-proxy.jp.oracle.com", 80)));
    }});


    public static String get(String url){

        return template.getForEntity(url,String.class).getBody();

    }

}
