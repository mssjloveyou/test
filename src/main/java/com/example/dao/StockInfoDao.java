package com.example.dao;
import com.example.entity.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface StockInfoDao extends JpaRepository<StockInfo,Long> {

    @Query("from StockInfo where createDate in (select max(createDate) from StockInfo)")
    List<StockInfo> getNewestData();

    @Query("from StockInfo where createDate=?1 and stockId in (select code from StockCode where id>4543)")
    List<StockInfo> getDataByDate( Date date);

//    @Modifying
//    @Query("update User set enable=false where id=:id")
//    void lockAccount(@Param("id")Integer id);
}
