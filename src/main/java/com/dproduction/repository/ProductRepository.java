package com.dproduction.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.dproduction.dto.ProductDto;
import com.dproduction.entity.Product;
import reactor.core.publisher.Flux;

public interface ProductRepository extends R2dbcRepository<Product, String> {

	Flux<ProductDto> findById(Long id);

	Flux<ProductDto> findByPriceBetween(double min, double max);

}
