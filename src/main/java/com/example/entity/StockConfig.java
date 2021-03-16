package com.example.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "STOCK_CONFIG")
public class StockConfig implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Integer id;
    @Column(name="value")
    private String value;
    @Column(name="NAME")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
