package ru.danyabereg.producer.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Builder
@Getter
public class ProductDto {
    private final String title;
    private final BigDecimal price;
    private final Integer quantity;
}
