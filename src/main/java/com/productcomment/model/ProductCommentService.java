package com.productcomment.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productCommentService")
public class ProductCommentService {
	
	@Autowired
	ProductCommentRepository repository;
	
	public void addProductComment(ProductCommentVO productCommentVO) {
		repository.save(productCommentVO);
	};
	
	public List<ProductCommentVO> getProductIdAll(String productId){
		return repository.findAllByProductId(productId);
	}
	
	public void updateProductComment(ProductCommentVO productCommentVO) {
		repository.save(productCommentVO);
	};
	
	public void deleteProductComment(Integer productCommentId) {
		if(repository.existsById(productCommentId)) {
			repository.deleteByProductCommentId(productCommentId);
		}
	}
	

}
