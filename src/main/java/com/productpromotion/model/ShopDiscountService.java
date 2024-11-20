package com.productpromotion.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ShopDiscountService {

	@Autowired
	ShopDiscountRepository shopDiscountRepository;
	
	public void addShopDiscount(ShopDiscountProjectVO shopDiscountProjectVO) {
		shopDiscountRepository.save(shopDiscountProjectVO);
	}
	
	public List<ShopDiscountProjectVO> getAll(){
		return shopDiscountRepository.findAll();
	}
	
	public ShopDiscountProjectVO getOneDiscount(Integer promotionId) {
		Optional<ShopDiscountProjectVO> optional = shopDiscountRepository.findById(promotionId);
		return optional.orElse(null);
	}
	
	public void updateShopDiscount(ShopDiscountProjectVO shopDiscountProjectVO) {
		shopDiscountRepository.save(shopDiscountProjectVO);
	}
}
