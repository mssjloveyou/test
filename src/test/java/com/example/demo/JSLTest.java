package com.example.demo;

import com.example.config.WebConfig;
import com.example.entity.StockCode;
import com.example.entity.StockInfo;
import com.example.service.StockCodeService;
import com.example.service.StockInfoService;
import com.example.util.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.config.WebConfig.CURRENT_PRICE;
import static com.example.config.WebConfig.SHSZ_COUNT;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest // 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
public class JSLTest {

    @Autowired
    private StockCodeService service;
    @Autowired
    private StockInfoService infoService;

    /**
     * 新增代码
     * @throws IOException
     */
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
    public void testJSL2() {
        try {
            for (int i = 1; i < 30; i++) {
                System.out.println(i);
                String url = "https://www.jisilu.cn/data/new_stock/super/?___jsl=LST___t=" + new Date().getTime()+"&page="+i+"&pageSize=100";
                String s = HttpUtil.get(url);

                ObjectMapper objectMapper = new ObjectMapper();
                HashMap hashMap = objectMapper.readValue(s.toString(), HashMap.class);
                ArrayList<HashMap<String, HashMap>> arr = (ArrayList) hashMap.get("rows");

                List<StockCode> list = service.findAll();
                List<String> codes = new ArrayList<>();
                for (StockCode tmpCode : list) {
                    codes.add(tmpCode.getCode());
                }
                for (HashMap<String, HashMap> tmpMap : arr) {
                    Map<String, Object> fundInfo = (Map<String, Object>) tmpMap.get("cell");
                    StockCode code = new StockCode();
                    code.setCode(fundInfo.get("stock_cd").toString().startsWith("51") ? "sh" + fundInfo.get("stock_cd").toString() : "sz" + fundInfo.get("stock_cd").toString());
                    code.setName(fundInfo.get("stock_nm").toString());
                    if (codes.indexOf(code.getCode()) < 0) {
                        service.save(code);
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @org.junit.Test
    public void testHistory() throws IOException, ParseException {

        List<StockCode> list = service.findAll();
        List<StockInfo> rs = new LinkedList<>();
        for(StockCode code : list){
            String str =code.getCode().replaceAll("sh","cn_").replaceAll("sz","cn_");
            try {
                rs.addAll(saveStockInfo(str, code.getCode()));
            }catch (NullPointerException ex){
                System.out.println("ignore");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(rs.size() > 1000){
                infoService.save(rs);
                rs = new LinkedList<>();
            }
        }
        if(rs.size() > 0){
            infoService.save(rs);
        }
    }

    private Collection<? extends StockInfo> saveStockInfo(String codeBuilder, String code) throws com.fasterxml.jackson.core.JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");
        String url = WebConfig.HISTORY_DATA.replace(":code",codeBuilder.toString())
                .replace(":start","20250616").replace(":end","20250617");
        System.out.println(url);
        String value = HttpUtil.get(url);
        if(value.length()<10 || value.contains("non-existent")){
            return null;
        }
        List<Map<String,Object>> resultList = mapper.readValue(value,List.class);
        List<StockInfo> rs = new LinkedList<>();
        for(Map<String,Object> tmp : resultList){
            List<List> values = (List<List>) tmp.get("hq");
            if(CollectionUtils.isEmpty(values)){
                continue;
            }
            for(List child : values){
                StockInfo info = new StockInfo();
                info.setStockId(tmp.get("code").toString().replaceAll("cn_",""));
                info.setStockId(code);
                info.setOpenPrice(Double.valueOf(child.get(1).toString()));
                info.setClosePrice(Double.valueOf(child.get(2).toString()));
                info.setDealHands(Double.valueOf(child.get(7).toString()));
                info.setDealMoney(Double.valueOf(child.get(8).toString()));
                info.setCreateDate(format.parse(child.get(0).toString()));
                info.setMonth(format2.format(info.getCreateDate()));
                rs.add(info);
            }

        }
        return rs;
    }

    @Test
    public void reviseCode(){
        List<StockCode> all = service.findAll();
        System.out.println(all.size());
        all.forEach(c->{
            try{
                if(StringUtils.isEmpty(c.getName())) {
                    String s = HttpUtil.get(CURRENT_PRICE.replace(":code", c.getCode()));
                    if (StringUtils.isEmpty(s) || s.length() < 50) {
                        System.out.println(c.getCode() + "-----");
                        if (c.getCode().startsWith("sh")) {
                            s = HttpUtil.get(CURRENT_PRICE.replace(":code", c.getCode().replace("sh", "sz")));
                            c.setCode(c.getCode().replace("sh", "sz"));
                        } else {
                            HttpUtil.get(CURRENT_PRICE.replace(":code", c.getCode().replace("sz", "sh")));
                            c.setCode(c.getCode().replace("sz", "sh"));
                        }
                    }
                    String name = s.split("=\"")[1].split(",")[0];
                    c.setName(name);
                    service.save(c);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws JsonProcessingException {

    }
}
