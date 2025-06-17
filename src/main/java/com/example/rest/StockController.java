package com.example.rest;


import com.example.config.WebConfig;
import com.example.entity.StockCode;
import com.example.entity.StockConfig;
import com.example.entity.StockInfo;
import com.example.service.StockCodeService;
import com.example.service.StockConfigService;
import com.example.service.StockInfoService;
import com.example.util.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class StockController {

    @Autowired
    private StockCodeService codeService;
    @Autowired
    private StockInfoService infoService;
    @Autowired
    private StockConfigService configService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");

    @GetMapping("/stock/code/update")
    public void updateCode() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String value = HttpUtil.get(WebConfig.CODE_URL);
        Map<String, Object> map = mapper.readValue(value, Map.class);

        List<Map<String, Object>> childMap = (List<Map<String, Object>>) map.get("rows");
        StockCode code = null;
        List<StockCode> list = codeService.findAll();
        List<String> codes = new ArrayList<>();
        for (StockCode tmpCode : list) {
            codes.add(tmpCode.getCode());
        }
        for (Map<String, Object> tmpMap : childMap) {
            Map<String, Object> fundInfo = (Map<String, Object>) tmpMap.get("cell");
            code = new StockCode();
            code.setCode(fundInfo.get("fund_id").toString().startsWith("51") ? "sh" + fundInfo.get("fund_id").toString() : "sz" + fundInfo.get("fund_id").toString());
            code.setName(fundInfo.get("fund_nm").toString());
            if (codes.indexOf(code.getCode()) < 0) {
                codeService.save(code);
            }
        }
    }

    @Scheduled(cron = "0 10 17 * * ?")
    @GetMapping("/stock/recommend")
    public void recommend(){

        infoService.recommend();

    }

    @GetMapping("/stock/update")
     @Scheduled(cron = "0 10 15 * * ?")
    public void updateInfo() throws ParseException {
        List<StockCode> list = codeService.findAll();
        List<StockInfo> stockInfos = infoService.getNewestData();
        List<String> codes = new ArrayList<>();
        Map<String, Date> newestData = new HashMap<>();
        for (StockInfo info : stockInfos) {
            newestData.put(info.getStockId(), info.getCreateDate());
        }
        for (StockCode code : list) {
            codes.add(code.getCode());
        }
        Map<Integer,List<String>> loopMaps = new HashMap<>();
        if(codes.size() > 1000){
            int groupSize = codes.size()/1000+1;

            loopMaps =
                    IntStream.range(0, (int) Math.ceil((double) codes.size() / groupSize))
                            .boxed()
                            .collect(Collectors.toMap(
                                    i -> i, // Key为分组的序号
                                    i -> codes.stream()
                                            .skip(i * groupSize)
                                            .limit(groupSize)
                                            .collect(Collectors.toList()) // Value为分组后的子列表
                            ));


        }else{
            loopMaps.put(1, codes);
        }

        for(int key : loopMaps.keySet()){
            String requestCodes = String.join(",", loopMaps.get(key));
            String obj = HttpUtil.get(WebConfig.CURRENT_PRICE.replace(":code", requestCodes));
            List<StockInfo> transitBeans = new LinkedList<>();
            for (String child : obj.split(";")) {
                if (child.split(",").length < 30) {
                    continue;
                }
                System.out.println(child);
                String value = child.substring(child.indexOf("str_") + 4);
                String name = value.substring(0, value.indexOf("="));
                String[] childrens = value.split(",");
                StockInfo info = new StockInfo();
                info.setStockId(name);
                info.setOpenPrice(Double.valueOf(childrens[1]));
                info.setClosePrice(Double.valueOf(childrens[3]));
                info.setDealHands(Double.valueOf(childrens[8]) / 100);
                info.setDealMoney(Double.valueOf(childrens[9]) / 10000);
                info.setCreateDate(format.parse(childrens[30]));
                info.setMonth(format2.format(info.getCreateDate()));
                if (newestData.get(info.getStockId()) == null || newestData.get(info.getStockId()).compareTo(info.getCreateDate()) != 0) {
                    transitBeans.add(info);
                }
            }
            infoService.save(transitBeans);
        }

    }


    @GetMapping("/stock/etf/{date}")
    public Map<String, List> getFluctuateData(@PathVariable("date") String date) throws ParseException {
        List<StockConfig> configs = configService.getStockConfigByType("calculateDate");
        String oldData = configs.get(0).getValue();
        if (!oldData.equals(date)) {
            configService.updateValueByType(date, "calculateDate");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        List<StockInfo> infoList = infoService.getDataByDate(dateFormat.parse(date));
        if(infoList.size() == 0){
            return new HashMap<>();
        }
        List<StockCode> codeList = codeService.getValuableCode();
        Map<String, String> nameMap = new HashMap<>();
        List<String> requestCodes = new ArrayList<>();
        Map<String, Double> openPriceMap = new HashMap<>();
        for (StockCode code : codeList) {
            nameMap.put(code.getCode(), code.getName());
            requestCodes.add(code.getCode());
        }
        for (StockInfo info : infoList) {
            openPriceMap.put(info.getStockId(), info.getClosePrice());
        }

        Map<Integer,List<String>> loopMaps = new HashMap<>();
        if(requestCodes.size() > 1000){
            int groupSize = requestCodes.size()/1000+1;

            loopMaps =
                    IntStream.range(0, (int) Math.ceil((double) requestCodes.size() / groupSize))
                            .boxed()
                            .collect(Collectors.toMap(
                                    i -> i, // Key为分组的序号
                                    i -> requestCodes.stream()
                                            .skip(i * groupSize)
                                            .limit(groupSize)
                                            .collect(Collectors.toList()) // Value为分组后的子列表
                            ));


        }else{
            loopMaps.put(1, requestCodes);
        }

        List<Map> result = new ArrayList<>();
        List<Map> todayResult = new ArrayList<>();
        for(int key : loopMaps.keySet()){
            String requestUrl = WebConfig.CURRENT_PRICE.replace(":code", String.join(",", loopMaps.get(key)));
            System.out.println(requestUrl);
            String currentPrices = HttpUtil.get(requestUrl);
            System.out.println(currentPrices);
            for (String child : currentPrices.split(";")) {
                if (child.split(",").length < 30) {
                    continue;
                }
                String value = child.substring(child.indexOf("str_") + 4);
                String name = value.substring(0, value.indexOf("="));
                String[] childrens = value.split(",");
                Double currentPrice = Double.valueOf(childrens[3]);
                Double todayOpenPrice = Double.valueOf(childrens[2]);
                Double openPrice = openPriceMap.get(name);
                if (openPrice != null && currentPrice != null && todayOpenPrice != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", name);
                    map.put("name", nameMap.get(name));
                    map.put("currentPrice", getDoubleValue(currentPrice));
                    map.put("fluctuate", getDoubleValue((currentPrice - openPrice) / openPrice * 100));
                    map.put("todayFluctuate", getDoubleValue((currentPrice - todayOpenPrice) / todayOpenPrice * 100));
                    result.add(map);
                    todayResult.add(map);
                }
            }
        }

        Collections.sort(result, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                return Double.valueOf(o2.get("fluctuate").toString()).compareTo(Double.valueOf(o1.get("fluctuate").toString()));
            }
        });
        Collections.sort(todayResult, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                return Double.valueOf(o2.get("todayFluctuate").toString()).compareTo(Double.valueOf(o1.get("todayFluctuate").toString()));

            }
        });
        Map<String, List> mp = new HashMap<>();
        mp.put("top", result);
        mp.put("today", todayResult);
        return mp;

    }

    private Double getDoubleValue(Double value) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @GetMapping("/stock/calculateDate")
    public String getLastCalculateDate() {
        String defaultValue = "20200630";
        List<StockConfig> list = configService.getStockConfigByType("calculateDate");
        if (list.size() == 0) {
            StockConfig config = new StockConfig();
            config.setType("calculateDate");
            config.setValue(defaultValue);
            configService.save(config);
            return defaultValue;
        }
        return list.get(0).getValue();
    }

    @GetMapping("/stock/shszcount")
    public List<Map> count() {
        String obj = HttpUtil.get(WebConfig.SHSZ_COUNT);
        List<Map> result = new ArrayList<>();
        for (String child : obj.split(";")) {
            if (child.length() < 10) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            String value = child.substring(child.indexOf("str_") + 4);
            String code = value.substring(0, value.indexOf("="));
            String[] childrens = value.split(",");
            map.put("code", code);
            map.put("name", childrens[0].replaceAll(code, "").replaceAll("=\"", ""));
            map.put("index", childrens[1]);
            map.put("count", childrens[5].replaceAll("\"", ""));
            result.add(map);
        }
        return result;
    }
}
