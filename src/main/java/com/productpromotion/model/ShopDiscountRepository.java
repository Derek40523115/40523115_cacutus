package com.productpromotion.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface ShopDiscountRepository extends JpaRepository<ShopDiscountProjectVO, Integer>{

	@Query(value = "select * from shop_discount_project where ?1 between promotion_started and promotion_end", nativeQuery = true)
	List<ShopDiscountProjectVO> usePromotion(LocalDate today);
}
