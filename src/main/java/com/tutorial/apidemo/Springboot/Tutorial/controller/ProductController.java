package com.tutorial.apidemo.Springboot.Tutorial.controller;
/* Controller là nơi nhận request và xử lí dữ liệu rồi trả về response cho phía gửi */
import com.tutorial.apidemo.Springboot.Tutorial.model.Product;
import com.tutorial.apidemo.Springboot.Tutorial.model.ResponseObject;
import com.tutorial.apidemo.Springboot.Tutorial.repositories.ProductRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController /* Báo cho JavaSpring Class này là Controller*/
@RequestMapping(path = "/api/v1/Products") /* Định tuyến routing */
public class ProductController {
    // DI = Dependency Injection
    @Autowired
    private ProductRespository productRespository;

    @GetMapping("")
    // this request is: http://localhost:8080/api/v1/Products
    List<Product> getAllProducts() {
        return productRespository.findAll(); // Where is data
    }

    @GetMapping("/{id}")
    // Let's return an object with: data, message, status
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = productRespository.findById(id);
        return foundProduct.isPresent() ?
            ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query product successfully", foundProduct)
            ):
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "Cannot find product with id = " + id, foundProduct)
            );
    }
    // insert new Prodcut with POST method
    // Postman: Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        // 2 product must not have the same name
        List<Product> foundProducts = productRespository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "insert Product succesfully", productRespository.save(newProduct))
        );
    }

    //update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updateProduct = productRespository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    return productRespository.save(product);
                }).orElseGet(() -> {
                    return productRespository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Product succesfully", updateProduct)
        );
    }

    // Delete a Product => DELETE method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = productRespository.existsById(id);
        if(exists) {
            productRespository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete product successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "Cannot find product to delete", "")
        );
    }
}
