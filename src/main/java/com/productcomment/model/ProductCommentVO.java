package com.productcomment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.member.model.MemberVO;
import com.product.model.ProductVO;

@Entity
@Table(name="product_comment")
public class ProductCommentVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_comment_id")
	private Integer productCommentId;
	
	@Column(name="product_comment")
	@NotNull(message="商品評論: 請勿空白")
	private String productComment;
	
	@ManyToOne
	@JoinColumn(name="member_id", referencedColumnName="member_id")
	private MemberVO member;
	
	@ManyToOne
//	@JsonBackReference  // 子對象使用這個註解
	@JoinColumn(name="product_id", referencedColumnName="product_id")
	private ProductVO product;
	
	@Column(name="star")
	private Integer star;

	
	
	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Integer getProductCommentId() {
		return productCommentId;
	}

	public void setProductCommentId(Integer productCommentId) {
		this.productCommentId = productCommentId;
	}

	public String getProductComment() {
		return productComment;
	}

	public void setProductComment(String productComment) {
		this.productComment = productComment;
	}

	public MemberVO getMember() {
		return member;
	}

	public void setMember(MemberVO member) {
		this.member = member;
	}

	public ProductVO getProduct() {
		return product;
	}

	public void setProduct(ProductVO product) {
		this.product = product;
	}
	
//	@Override
//	public String toString() {
//		
//		return ;
//	}
	
	
	
	
}
