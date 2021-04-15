package com.wei.spring_boot_practice.service;

import com.wei.spring_boot_practice.entity.Product;
import com.wei.spring_boot_practice.exception.ConflictException;
import com.wei.spring_boot_practice.exception.NotFoundException;
import com.wei.spring_boot_practice.prameter.ProductQueryParameter;
import com.wei.spring_boot_practice.repository.MockProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private MockProductDAO productDAO;

    public Product createProduct(Product request) {
        boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
        if (isIdDuplicated) {
            throw new ConflictException("The id of the product is duplicated.");
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productDAO.insert(product);
    }

    public Product getProduct(String id) {
        return productDAO.find(id).orElseThrow(() -> new NotFoundException("Can not find the product."));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productDAO.replace(product.getId(), request);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product.getId());
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        return productDAO.find(param);
    }
}
