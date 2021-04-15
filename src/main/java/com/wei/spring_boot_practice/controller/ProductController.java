package com.wei.spring_boot_practice.controller;

import com.wei.spring_boot_practice.entity.Product;
import com.wei.spring_boot_practice.prameter.ProductQueryParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

//import java.awt.*;

@RestController
//set "/products" as default path
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

//    database simulation
    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct
    public void initDB(){
        productDB.add(new Product("T0001","Trading Holy Grail", 200));
        productDB.add(new Product("T0002","Trading FX", 180));
        productDB.add(new Product("T0003","Finance Management", 360));
        productDB.add(new Product("T0004","Guns, Germs, and Steel", 540));
        productDB.add(new Product("T0005","Billion Whale", 320));
    }
//  GET "/products/{id}"  get product data
//  can be replace by @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){
        Optional<Product> productOp = productDB.stream().filter(p -> p.getId().equals(id)).findFirst();

        if(!productOp.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Product product = productOp.get();

        return ResponseEntity.ok().body(product);
    }

//  POST "/products" create new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product request) {
        boolean isIdDuplicated = productDB.stream().anyMatch(p -> p.getId().equals(request.getId()));

        if (isIdDuplicated) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(location).body(product);
    }

//  PUT "/products/{id}"  update product data
    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(@PathVariable("id") String id, @RequestBody Product request){
        Optional<Product> productOp = productDB.stream().filter(p -> p.getId().equals(id)).findFirst();

        if (!productOp.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Product product = productOp.get();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return ResponseEntity.ok().body(product);
    }

//  DELETE "/products/{id}"  delete product data
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id){
        boolean isRemoved = productDB.removeIf(p -> p.getId().equals(id));

        if (isRemoved){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param){
        String nameKeyword = param.getKeyword();
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        Comparator<Product> comparator = Objects.nonNull(orderBy)&&Objects.nonNull(sortRule)
                ? configureSortComparator(orderBy,sortRule)
                : (p1,p2) -> 0;

        List<Product> products = productDB.stream()
                .filter(p -> p.getName().toUpperCase().contains(nameKeyword.toUpperCase()))
                .sorted(comparator)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(products);
    }

//    an instance of Comparator to set the sorting rule
    private Comparator<Product> configureSortComparator(String orderBy, String sortRule){
        Comparator<Product> comparator = (p1, p2) -> 0;

        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (orderBy.equalsIgnoreCase("name")) {
//            colon is one way to call in lambda expression
            comparator = Comparator.comparing(Product::getName);
        }

        if (sortRule.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        return comparator;

    }

}

