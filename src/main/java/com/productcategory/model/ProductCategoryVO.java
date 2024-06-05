package com.productcategory.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.product.model.ProductVO;

@Entity
@Table(name="product_category")
public class ProductCategoryVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_category_id")
//	@NotNull(message="商品編號: 請勿空白")
	private Integer productCategoryId;
	
	@OneToMany(mappedBy = "productCategoryVO")
	@OrderBy("productId asc")//新增的有可能出錯
	private Set<ProductVO> productVOs;
	
	@Column(name = "product_category_name")
	@NotEmpty(message="類別名稱: 請勿空白")
	private String productCategoryName;
	
	
	public Integer getProductCategoryId() {
		return productCategoryId;
	}
	
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	
	
	public Set<ProductVO> getProductVOs() {
		return productVOs;
	}

	public void setProductVOs(Set<ProductVO> productVOs) {
		this.productVOs = productVOs;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}
	
}
