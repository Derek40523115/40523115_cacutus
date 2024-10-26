package com.productphoto.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productPhotoService")
public class ProductPhotoService {
	
	@Autowired
	ProductPhotoRepository repository;
	
	public void addProductPhoto(ProductPhotoVo productPhotoVo) {
		repository.save(productPhotoVo);
	}
	
	public void updateProductPhoto(ProductPhotoVo productPhotoVo) {
		repository.save(productPhotoVo);
	}
	
	
	public ProductPhotoVo getOneProduct(Integer productPhotoId) {
		Optional<ProductPhotoVo> optional =  repository.findById(productPhotoId);
		return optional.orElse(null);
	}
	
	public List<ProductPhotoVo> getAll(){
		return repository.findAll();
	}
}
