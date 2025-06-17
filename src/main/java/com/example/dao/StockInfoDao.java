package com.example.dao;
import com.example.entity.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface StockInfoDao extends JpaRepository<StockInfo,Long> {

    @Query("from StockInfo where createDate in (select max(createDate) from StockInfo)")
    List<StockInfo> getNewestData();

    @Query("from StockInfo where createDate=?1 and stockId in (select code from StockCode where name like '%ETF%')")
    List<StockInfo> getDataByDate( Date date);

    /**
     * 交易量推荐
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert ignore into monitor select a.stock_id, (select create_date from stock_info order by id desc limit 1) from (   select  * from stock_info a join (select code from stock_code where id< 4543)b on a.stock_id=b.code    where a.create_date=(select create_date from stock_info order by id desc limit 1) and a.deal_money<50000 ) a left join ( select stock_id,avg(deal_hands) deal_hands, avg(close_price) close_price,avg(deal_money) deal_money,max(close_price) max_close_price from stock_info where deal_hands>100 group by stock_id ) b on a.stock_id=b.stock_id where a.deal_hands >= b.deal_hands * 3 and a.close_price*2<b.max_close_price and b.max_close_price<30")
    void recommend();


//    @Modifying
//    @Query("update User set enable=false where id=:id")
//    void lockAccount(@Param("id")Integer id);
}
