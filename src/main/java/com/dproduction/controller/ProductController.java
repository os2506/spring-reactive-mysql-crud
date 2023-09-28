package com.dproduction.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dproduction.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.dproduction.dto.ProductDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/products")
public class ProductController {

	private ProductService prodService;

	public ProductController(ProductService prodService) {
		this.prodService = prodService;
	}

	@GetMapping
	public Flux<ProductDto> getProducts() {
		return prodService.getProducts();
	}

	@GetMapping("/{id}")
	public Mono<ProductDto> getProduct(@PathVariable String id) {
		return prodService.getProduct(id);
	}

	@PostMapping
	public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
		System.out.println("controller method called ...");
		return prodService.saveProduct(productDtoMono);
	}

	@PutMapping("/{id}")
	public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable String id) {
		return prodService.updateProduct(productDtoMono, id);
	}

	@DeleteMapping("/{id}")
	public Mono<Void> deleteProduct(@PathVariable String id) {
		return prodService.deleteProduct(id);
	}

	@GetMapping("/product-range")
	public Flux<ProductDto> getProductBetweenRange(@RequestParam("min") double min, @RequestParam("max") double max) {
		return prodService.getProductInRange(min, max);
	}

}
