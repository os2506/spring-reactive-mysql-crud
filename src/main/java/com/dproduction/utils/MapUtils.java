package com.dproduction.utils;


import com.dproduction.dto.ProductDto;
import com.dproduction.entity.Product;

public class MapUtils {

	/// Entity -> Dto
	public static ProductDto entityToDto(Product product) {
		ProductDto prodDto = new ProductDto();
		prodDto.setId(product.getId());
		prodDto.setName(product.getName());
		prodDto.setQty(product.getQty());
		prodDto.setPrice(product.getPrice());
		// BeanUtils.copyProperties(product, prodDto);
		return prodDto;
	}

	public static Product dtoToEntity(ProductDto productDto) {
		Product product = new Product();
		product.setId(productDto.getId());
		product.setName(productDto.getName());
		product.setQty(productDto.getQty());
		product.setPrice(productDto.getPrice());
		return product;
	}

}
