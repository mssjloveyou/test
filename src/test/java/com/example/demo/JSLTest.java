package com.example.demo;

import com.example.config.WebConfig;
import com.example.entity.StockCode;
import com.example.entity.StockInfo;
import com.example.service.StockCodeService;
import com.example.service.StockInfoService;
import com.example.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest // 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
public class JSLTest {

    @Autowired
    private StockCodeService service;
    @Autowired
    private StockInfoService infoService;
    @org.junit.Test
    public void testJSL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String value = HttpUtil.get(WebConfig.CODE_URL);
        Map<String,Object> map = mapper.readValue(value,Map.class);

        List<Map<String,Object>>  childMap = (List<Map<String, Object>>) map.get("rows");
        StockCode code = null;
        List<StockCode> list = service.findAll();
        List<String> codes = new ArrayList<>();
        for(StockCode tmpCode : list){
            codes.add(tmpCode.getCode());
        }
        for(Map<String,Object> tmpMap : childMap){
            Map<String,Object> fundInfo = (Map<String, Object>) tmpMap.get("cell");
            code = new StockCode();
            code.setCode(fundInfo.get("fund_id").toString().startsWith("51")?"sh"+fundInfo.get("fund_id").toString():"sz"+fundInfo.get("fund_id").toString());
            code.setName(fundInfo.get("fund_nm").toString());
            if(codes.indexOf(code.getCode())<0){
                service.save(code);
            }
        }
    }

    @org.junit.Test
    public void testHistory() throws IOException, ParseException {

        List<StockCode> list = service.findAll();
        StringBuilder codeBuilder = new StringBuilder();
        int i=0;
        for(StockCode code : list){
            String str =code.getCode().replaceAll("sh","cn_").replaceAll("sz","cn_");
            saveStockInfo(str);
        }

//

    }

    private void saveStockInfo(String codeBuilder) throws com.fasterxml.jackson.core.JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String url = WebConfig.HISTORY_DATA.replace(":code",codeBuilder.toString())
                .replace(":start","20200925").replace(":end","20201010");
        System.out.println(url);
        String value = HttpUtil.get(url);
        if(value.length()<10){
            return;
        }
        List<Map<String,Object>> resultList = mapper.readValue(value,List.class);
        for(Map<String,Object> tmp : resultList){
            List<List> values = (List<List>) tmp.get("hq");
            System.out.println(tmp.get("code").toString());
            for(List child : values){
                StockInfo info = new StockInfo();
                info.setStockId(tmp.get("code").toString().replaceAll("cn_",""));
                info.setStockId(info.getStockId().startsWith("51")?"sh"+info.getStockId():"sz"+info.getStockId());
                info.setOpenPrice(Double.valueOf(child.get(1).toString()));
                info.setClosePrice(Double.valueOf(child.get(2).toString()));
                info.setDealHands(Double.valueOf(child.get(7).toString()));
                info.setDealMoney(Double.valueOf(child.get(8).toString()));
                info.setCreateDate(format.parse(child.get(0).toString()));
                infoService.save(info);
            }
        }
    }


}
