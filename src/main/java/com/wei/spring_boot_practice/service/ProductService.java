package com.wei.spring_boot_practice.service;

import com.wei.spring_boot_practice.converter.ProductConverter;
import com.wei.spring_boot_practice.entity.Product;
import com.wei.spring_boot_practice.entity.ProductRequest;
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

    public Product createProduct(ProductRequest request) {
        Product product = ProductConverter.toProduct(request);
        return repository.insert(product);
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Can not find the product."));
    }

    public Product replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);

        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());

        return repository.save(newProduct);
    }

    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = configureSort(param.getOrderBy(), param.getSortRule());
//        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyword);
        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyword, sort);
    }

    private Sort configureSort(String orderBy, String sortRule) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
//            The second parameter of Sort should be List
//            sort = new Sort(direction, orderBy);
        }

        return sort;
    }
}
