package com.example.demo;

import com.example.entity.StockCode;
import com.example.service.StockCodeService;
import com.example.service.StockInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
//@SpringBootTest // 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
public class StockInfoTest {
    @Autowired
    private StockInfoService service;
    @Autowired
    private StockCodeService codeService;


    @Test
    public void testSave() throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
////        List<StockCode> list = codeService.getValuableCode(dateFormat.parse("20200701"));
//
//        System.out.println(service.getDataByDate(dateFormat.parse("20200701")).size());

        Long test = System.currentTimeMillis();
        System.out.println(new Date(test));
        System.out.println(new Date(test+60000));




    }




}
