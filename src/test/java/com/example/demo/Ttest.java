package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Ttest {


    public static void main(String[] args) throws InterruptedException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(dateFormat.parse("20210803"));
    }
}
