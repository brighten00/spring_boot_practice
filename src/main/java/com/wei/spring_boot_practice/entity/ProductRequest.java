package com.wei.spring_boot_practice.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductRequest {
    private String id;


    @NotEmpty(message = "Product name is not defined")
    private String name;


    @NotNull
    @Min(value = 0, message = "price should be positive or 0")
    private Integer price;

    public ProductRequest(){

    }

    public ProductRequest(String id, String name, Integer price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

//    public String getId(){
//        return id;
//    }
//
//    public void setId(String id){
//        this.id = id;
//    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getPrice(){
        return price;
    }

    public void setPrice(Integer price){
        this.price = price;
    }
}
