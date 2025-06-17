package com.example.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "STOCK_INFO")
public class StockInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    public Integer id;

    @Column(name = "STOCK_ID")
    private String stockId;


    @Column(name="OPEN_PRICE")
    private Double openPrice;

    @Column(name="CLOSE_PRICE")
    private Double closePrice;

    @Column(name="DEAL_HANDS")
    private Double dealHands;

    @Column(name="DEAL_MONEY")
    private Double dealMoney;

    @Column(name="CREATE_DATE")
    private Date createDate;
    @Column(name="month")
    private String month;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getDealHands() {
        return dealHands;
    }

    public void setDealHands(Double dealHands) {
        this.dealHands = dealHands;
    }

    public Double getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(Double dealMoney) {
        this.dealMoney = dealMoney;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
