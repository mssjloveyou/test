package com.example.service;

import com.example.dao.StockInfoDao;
import com.example.entity.StockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StockInfoService {
    @Autowired
    private StockInfoDao stockInfoDao;


    public void save(StockInfo info){
        stockInfoDao.save(info);
    }

    public void save(List<StockInfo> list){
        stockInfoDao.saveAll(list);
    }

    public List<StockInfo> getNewestData(){
        return stockInfoDao.getNewestData();
    }


    public List<StockInfo> getDataByDate(Date date) {

        return stockInfoDao.getDataByDate(date);
    }
}
