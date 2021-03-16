package com.example.config;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;

public class WebConfig {

    public static String CODE_URL="https://www.jisilu.cn/data/etf/etf_list/?___jsl=LST___t="+new Date().getTime()+"&volume=&unit_total=&rp=25";

    public static String HISTORY_DATA="https://q.stock.sohu.com/hisHq?code=:code&start=:start&end=:end";

    public static String CURRENT_PRICE="http://hq.sinajs.cn/list=:code";

    public static String SHSZ_COUNT="http://hq.sinajs.cn/rn="+new Date().getTime()+"&list=s_sh000001,s_sz399001";


}