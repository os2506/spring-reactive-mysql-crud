package com.dproduction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import com.dproduction.dto.ProductDto;
import com.dproduction.service.ProductService;
import com.dproduction.controller.ProductController;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringWebfluxApplicationTests {

	@Autowired
	private WebTestClient wbTestClient;

	@MockBean
	private ProductService service;

	@Test
	public void addProductTest() {

		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(23L, "productTest1", 15, 119.99));

		when(service.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		wbTestClient.post().uri("/products").body(Mono.just(productDtoMono), ProductDto.class).exchange().expectStatus()
				.isOk();// 200

	}

	@Test
	public void getProductsTest() {
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto(20L, "product20", 30, 65.99),
				new ProductDto(21L, "product37", 30, 70.99));

		when(service.getProducts()).thenReturn(productDtoFlux);

		Flux<ProductDto> responseBody = wbTestClient.get().uri("/products").exchange().expectStatus().isOk()
				.returnResult(ProductDto.class).getResponseBody();

		StepVerifier.create(responseBody).expectSubscription().assertNext(dto -> {
			Assertions.assertThat(dto.getId()).isEqualTo(20L);
			Assertions.assertThat(dto.getName()).isEqualTo("product20");
			Assertions.assertThat(dto.getQty()).isEqualTo(30);
			Assertions.assertThat(dto.getPrice()).isCloseTo(65.99, Assertions.offset(0.01));
		}).assertNext(dto -> {
			Assertions.assertThat(dto.getId()).isEqualTo(21L);
			Assertions.assertThat(dto.getName()).isEqualTo("product37");
			Assertions.assertThat(dto.getQty()).isEqualTo(30);
			Assertions.assertThat(dto.getPrice()).isCloseTo(70.99, Assertions.offset(0.01));
		}).verifyComplete();

	}

	@Test
	public void getProductTest() {
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(20L, "product20", 30, 65.99));
		when(service.getProduct(any())).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody = wbTestClient.get().uri("/products/20").exchange().expectStatus().isOk()
				.returnResult(ProductDto.class).getResponseBody();

		StepVerifier.create(responseBody).expectSubscription().expectNextMatches(p -> p.getName().equals("product20"))
				.verifyComplete();
	}

	@Test
	public void updateProductTest() {
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(20L, "product20", 30, 65.99));
		when(service.updateProduct(productDtoMono, "20")).thenReturn(productDtoMono);

		// update url
		wbTestClient.put().uri("/products/20").body(Mono.just(productDtoMono), ProductDto.class).exchange()
				.expectStatus().isOk();// 200
	}

	@Test
	public void deleteProductTest() {
		given(service.deleteProduct(any())).willReturn(Mono.empty());
		wbTestClient.delete().uri("/products/102").exchange().expectStatus().isOk();// 200
	}

}
