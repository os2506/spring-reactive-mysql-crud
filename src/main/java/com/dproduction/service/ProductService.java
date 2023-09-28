package com.dproduction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.data.domain.Range;
import com.dproduction.dto.ProductDto;
import com.dproduction.entity.Product;
import com.dproduction.repository.ProductRepository;
import com.dproduction.utils.MapUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	public Flux<ProductDto> getProducts() {
		return repository.findAll().map(MapUtils::entityToDto);
	}

	public Mono<ProductDto> getProduct(String id) {
		// Flux<Integer> originalFlux = Flux.just(1, 2, 3, 4, 5);
		// Flux<String> transformedFlux = originalFlux.map(number -> "Number " +
		// number);
		// transformedFlux.subscribe(System.out::println);
		return repository.findById(id).map(MapUtils::entityToDto);
	}

	public Flux<ProductDto> getProductInRange(double min, double max) {
		return repository.findByPriceBetween(min, max);
	}

	@Transactional
	public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
		System.out.println("service method called ...");
		return productDtoMono.map(MapUtils::dtoToEntity).flatMap(repository::save).map(MapUtils::entityToDto);
	}

	/*
	 * @Transactional public Mono<ProductDto> updateProduct(Mono<ProductDto>
	 * productDtoMono,Long id){ return repository.findById(id)
	 * .flatMap(p->productDtoMono.map(MapUtils::dtoToEntity) //
	 * .doOnNext(e->e.setId(id))) .flatMap(repository::save)
	 * .map(MapUtils::entityToDto); }
	 */

	@Transactional
	public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
		// Find the existing product by id
		return repository.findById(id).flatMap(existingProduct -> {
			// Map the ProductDto to a Product entity and set id
			return productDtoMono.map(productDto -> {
				Product updatedProduct = MapUtils.dtoToEntity(productDto);
				updatedProduct.setId(existingProduct.getId());
				return updatedProduct;
			});
		}).flatMap(repository::save).map(MapUtils::entityToDto);
	}

	public Mono<Void> deleteProduct(String id) {
		return repository.deleteById(id);
	}

}
