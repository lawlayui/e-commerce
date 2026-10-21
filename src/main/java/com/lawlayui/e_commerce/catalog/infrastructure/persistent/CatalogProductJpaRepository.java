package com.lawlayui.e_commerce.catalog.infrastructure.persistent;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CatalogProductJpaRepository extends JpaRepository<CatalogProductJpa, String>{
    @Query("SELECT DISTINCT p FROM CatalogProductJpa p JOIN p.categories c " +
       "WHERE p.price BETWEEN :min AND :max " +
       "AND p.rating = :rating " +
       "AND LOWER(p.name) LIKE LOWER(:name) " +
       "AND LOWER(c) LIKE LOWER(:category)")
    List<CatalogProductJpa> findByFilters(
        @Param("min") BigDecimal min,
        @Param("max") BigDecimal max,
        @Param("rating") Double rating,
        @Param("name") String name,
        @Param("category") String category,
        Sort sort
        );
}
