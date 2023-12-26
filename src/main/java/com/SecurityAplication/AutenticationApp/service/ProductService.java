package com.SecurityAplication.AutenticationApp.service;

import com.SecurityAplication.AutenticationApp.DTO.ProductDTO;
import com.SecurityAplication.AutenticationApp.domain.product.Product;
import com.SecurityAplication.AutenticationApp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService  {

    private final ProductRepository  productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(()->new Exception("Produto não encontrado!"));
    }

    public Product updateProduct(ProductDTO product, Long id) throws  Exception{

        Optional<Product> entityOptional = productRepository.findById(id);
        if(entityOptional.isPresent()) {
            Product entity = entityOptional.get();
            entity.setProductName(product.productName());
            entity.setCod(product.cod());
            entity.setProductValue(product.productValue());
            entity.setQuantity(product.quantity());
            this.productRepository.save(entity);
            return entity;

        }
        throw new Exception("Produto não existe!");
    }

    public Product saveProduct(ProductDTO product){
        Product newProduct = new Product(product);
        this.productRepository.save(newProduct);
        return newProduct;
    }

    public  void deleteById(Long id) throws Exception {
        Optional<Product> oldEntity = productRepository.findById(id);
        if (!oldEntity.isPresent()) {
            throw new Exception("Produto não existe!");
        }
        productRepository.deleteById(id);
    }
 }


