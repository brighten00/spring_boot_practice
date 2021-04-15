package com.wei.spring_boot_practice.prameter;

public class ProductQueryParameter {
    private String keyword;
    private String orderBy;
    private String sortRule;

//    set default so "/products" can show every products
//    public ProductQueryParameter(){
//        this.keyword = "";
//        this.orderBy = "";
//        this.sortRule = "";
//    }

    public String getKeyword() {
        return keyword;
    }

//    when spring boot detect "keyword" the set method would be called

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortRule() {
        return sortRule;
    }

    public void setSortRule(String sortRule) {
        this.sortRule = sortRule;
    }
    
}
