package com.example.dao;
import com.example.entity.StockCode;
import com.example.entity.StockConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockConfigDao extends JpaRepository<StockConfig,Long> {

    @Query("from StockConfig where type=:type")
    List<StockConfig> getStockConfigByType(@Param("type") String type);

    @Modifying
    @Query("update StockConfig set value=:value where type=:type")
    void updateValueByType(@Param("value")String value,@Param("type")String type);

}
