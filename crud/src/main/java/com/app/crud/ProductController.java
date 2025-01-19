package com.app.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final List<Product> productList = new ArrayList<>();

    public ProductController(){
        productList.add(new Product(1,"Apple",12.75));
        productList.add(new Product(2, "Orange", 15.50));
        productList.add(new Product(3, "Mango", 20.75));
    }

    private Optional<Product> findProductById(int id) {
        return productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody Product product){
        productList.add(product);
        return new ResponseEntity<>(new Response("Product Added", product), HttpStatus.OK);
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        Optional<Product> product = findProductById(id);
        return product.map(value -> new ResponseEntity<>(new Response("Product with ID: " + value.getId() + " found.", value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response("Product not found", null), HttpStatus.NOT_FOUND));
    }


    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        if (productList.isEmpty()) {
            return new ResponseEntity<>(new Response("Product List Empty", null), HttpStatus.OK);
        }
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }


    @PutMapping("/updateProductById/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable int id, @RequestBody Product updatedProduct){
        Optional<Product> product = findProductById(id);
        if (product.isPresent()){
            updatedProduct.setId(id);
            int index = productList.indexOf(product.get());
            productList.set(index, updatedProduct);
            return new ResponseEntity<>(new Response("Product with ID: " + product.get().getId() +" updated successfully.", product.get()), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new Response("Product not found", null), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteProductById/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable int id){
        Optional<Product> product = findProductById(id);
        if(product.isPresent()){
            productList.remove(product.get());
            return new ResponseEntity<>(new Response("Product with ID: " + product.get().getId() +" deleted successfully.", product.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new Response("Product not found", null), HttpStatus.NOT_FOUND);
        }
    }
}
