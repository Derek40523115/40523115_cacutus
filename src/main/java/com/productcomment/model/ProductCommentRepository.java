package com.productcomment.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductCommentRepository extends JpaRepository<ProductCommentVO, Integer>{

	@Query(value = "select * from product_comment where product_id =?1", nativeQuery = true)
	List<ProductCommentVO> findAllByProductId(String productId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from product_comment where product_comment_id =?1", nativeQuery = true)
	void deleteByProductCommentId(int productCommentId);
}
