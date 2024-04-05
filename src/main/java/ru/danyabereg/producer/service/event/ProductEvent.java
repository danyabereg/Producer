package ru.danyabereg.producer.service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class ProductEvent {
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
