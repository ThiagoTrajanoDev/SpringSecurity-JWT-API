package com.SecurityAplication.AutenticationApp.controller;

import com.SecurityAplication.AutenticationApp.DTO.ProductDTO;
import com.SecurityAplication.AutenticationApp.domain.product.Product;
import com.SecurityAplication.AutenticationApp.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

        private  final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id) throws Exception {
        Product updatedProduct = this.productService.updateProduct(productDTO,id);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) throws Exception {
        Product product = this.productService.getById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct (@RequestBody ProductDTO productDTO){
        Product product = this.productService.saveProduct(productDTO);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity deleteProduct (@PathVariable Long id) throws Exception {
        this.productService.deleteById(id);
        return ResponseEntity.ok().build();

    }

}
