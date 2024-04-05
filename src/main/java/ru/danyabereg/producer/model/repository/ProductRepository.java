package ru.danyabereg.producer.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.danyabereg.producer.model.dto.ProductDto;
import ru.danyabereg.producer.model.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> deleteProductByTitleIgnoreCase(String title);

    Optional<Product> findByTitleIgnoreCase(String title);
}
