package com.wei.spring_boot_practice.controller;

import com.wei.spring_boot_practice.entity.Product;
import com.wei.spring_boot_practice.entity.ProductRequest;
import com.wei.spring_boot_practice.prameter.ProductQueryParameter;
import com.wei.spring_boot_practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

//import java.awt.*;

//for spring boot above 2.3 need to add org.springframework.validation to maven and @Validated
//reference: https://stackoverflow.com/questions/61959918/spring-boot-validations-stopped-working-after-upgrade-from-2-2-5-to-2-3-0
@Validated
@RestController
//set "/products" as default path
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
//@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private ProductService productService;

//  GET "/products/{id}"  get product data
//  can be replace by @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){
        Product product = productService.getProduct(id);
        return ResponseEntity.ok().body(product);
    }

//  POST "/products" create new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(location).body(product);
    }

//  PUT "/products/{id}"  update product data
    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(@PathVariable("id") String id,@Valid @RequestBody ProductRequest request){
        Product product = productService.replaceProduct(id, request);

        return ResponseEntity.ok().body(product);
    }

//  DELETE "/products/{id}"  delete product data
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id){
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();

    }

//    search products

//    "keyword" is the searching keyword after ip, ex:?keyword=test
//    @@20210413 get method without sorting
//    public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "keyword", defaultValue = "") String keyword){
//        List<Product> products = productDB.stream().filter(p -> p.getName().toUpperCase().contains(keyword.toUpperCase())).collect(Collectors.toList());
//
//        return ResponseEntity.ok().body(products);
//    }
//    @@20210413
//    GET "/products" get list of products
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param){
        List<Product> products = productService.getProducts(param);

        return ResponseEntity.ok().body(products);
    }


}

