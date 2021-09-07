package com.example.dao;
import com.example.entity.StockCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface StockCodeDao extends JpaRepository<StockCode,Long> {

    @Query("from StockCode where code in (select stockId from StockInfo where dealMoney>10000 and createDate =:date )")
    List<StockCode> getValuableCode(@Param("date") Date date);

}
