package com.example.service;

import com.example.dao.StockCodeDao;
import com.example.entity.StockCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StockCodeService {

    @Autowired
    private StockCodeDao stockCodeDao;

    public void save(StockCode info){
        stockCodeDao.save(info);
    }

    public List<StockCode> findAll(){
        return stockCodeDao.findAll();
    }


    public List<StockCode> getValuableCode() {
        return stockCodeDao.getValuableCode();
    }
}
