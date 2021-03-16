package com.example.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "STOCK_CODE")
public class StockCode implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Integer id;
    @Column(name="CODE")
    private String code;
    @Column(name="NAME")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
