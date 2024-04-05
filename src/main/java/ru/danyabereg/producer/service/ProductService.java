package ru.danyabereg.producer.service;

import ru.danyabereg.producer.model.dto.ProductDto;
import ru.danyabereg.producer.model.entity.Product;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto) throws ExecutionException, InterruptedException;

    ProductDto updateProduct(ProductDto productDto) throws ExecutionException, InterruptedException;

    ProductDto deleteProduct(String title) throws ExecutionException, InterruptedException;

    ProductDto readProduct(String title) throws ExecutionException, InterruptedException;
}
