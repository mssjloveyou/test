package com.example.service;

import com.example.dao.StockConfigDao;
import com.example.entity.StockCode;
import com.example.entity.StockConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StockConfigService {

    @Autowired
    private StockConfigDao stockCodeDao;

    public List<StockConfig> getStockConfigByType(String type){
        return stockCodeDao.getStockConfigByType(type);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void updateValueByType(String value,String type){
        stockCodeDao.updateValueByType(value,type);
    }


    public void save(StockConfig config) {
        stockCodeDao.save(config);
    }
}
