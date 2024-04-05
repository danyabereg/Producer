package ru.danyabereg.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danyabereg.producer.model.dto.ProductDto;
import ru.danyabereg.producer.model.entity.Product;
import ru.danyabereg.producer.model.repository.ProductRepository;
import ru.danyabereg.producer.service.event.ProductEvent;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;
    private final String topic = "product-events-topic";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String CREATED = "Created";
    private final String UPDATED = "Updated";
    private final String READ = "Read";
    private final String DELETED = "Deleted";

    public ProductServiceImpl(ProductRepository repository,
                              KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) throws ExecutionException, InterruptedException {
        repository.save(buildProduct(productDto));
        ProductEvent productEvent = buildEvent(productDto);

        SendResult<String, ProductEvent> result = kafkaTemplate
                .send(topic, CREATED, productEvent).get();

        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        LOGGER.info("Return: {}", CREATED);

        return productDto;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) throws ExecutionException, InterruptedException {
        Product product = repository.findByTitleIgnoreCase(productDto.getTitle()).orElseThrow();
        if (productDto.getPrice() != null)
            product.setPrice(productDto.getPrice());
        if (productDto.getQuantity() != null)
            product.setQuantity(productDto.getQuantity());
        ProductEvent productEvent = buildEvent(productDto);

        SendResult<String, ProductEvent> result = kafkaTemplate
                .send(topic, UPDATED, productEvent).get();

        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        LOGGER.info("Return: {}", UPDATED);

        return buildProductDto(product);
    }

    @Override
    public ProductDto deleteProduct(String title) throws ExecutionException, InterruptedException {
        ProductEvent productEvent = buildEvent(buildProductDto(
                repository.findByTitleIgnoreCase(title).orElseThrow()));
        ProductDto deleteResult = buildProductDto(repository.deleteProductByTitleIgnoreCase(title).get(0));

        SendResult<String, ProductEvent> result = kafkaTemplate
                .send(topic, DELETED, productEvent).get();

        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        LOGGER.info("Return: {}", DELETED);

        return deleteResult;
    }

    @Override
    public ProductDto readProduct(String title) throws ExecutionException, InterruptedException {
        ProductDto productDto = buildProductDto(repository.findByTitleIgnoreCase(title).orElseThrow());
        ProductEvent productEvent = buildEvent(productDto);

        SendResult<String, ProductEvent> result = kafkaTemplate
                .send(topic, READ, productEvent).get();

        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        LOGGER.info("Return: {}", READ);

        return productDto;
    }

    ProductEvent buildEvent(ProductDto productDto) {
        return ProductEvent.builder()
                .title(productDto.getTitle())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .build();
    }

    Product buildProduct(ProductDto productDto) {
        return Product.builder()
                .title(productDto.getTitle())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .build();
    }

    ProductDto buildProductDto(Product product) {
        return ProductDto.builder()
                .title(product.getTitle())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
