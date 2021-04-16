package com.wei.spring_boot_practice.service;

import com.wei.spring_boot_practice.entity.Product;
import com.wei.spring_boot_practice.exception.ConflictException;
import com.wei.spring_boot_practice.exception.NotFoundException;
import com.wei.spring_boot_practice.prameter.ProductQueryParameter;
import com.wei.spring_boot_practice.repository.MockProductDAO;
import com.wei.spring_boot_practice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Product createProduct(Product request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.insert(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Can not find the product."));
    }

    public Product replaceProduct(String id, Product request) {
        Product oldProduct = getProduct(id);

        Product product = new Product();
        product.setId(oldProduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.save(product);
    }

    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);

//        Sort sort = configureSort(param.getOrderBy(), param.getSortRule());
        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyword);
//        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyword, sort);
    }

//    private Sort configureSort(String orderBy, String sortRule) {
//        Sort sort = Sort.unsorted();
//        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
//            Sort.Direction direction = Sort.Direction.fromString(sortRule);
//            sort = new Sort(direction, orderBy);
//        }
//
//        return sort;
//    }
}
