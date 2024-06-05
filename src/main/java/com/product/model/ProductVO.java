package com.product.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.productcategory.model.ProductCategoryVO;
import com.shoporderdetail.model.ShopOrderDetailVO;

@Entity
@Table(name="product")
public class ProductVO {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", updatable = false)
	private Integer productId;

	
//	@OneToMany(mappedBy = "productVO", cascade = CascadeType.ALL)
//	private Set<ProductPhotoVo> ProductPhotoVos;
	
	//圖片改放入商品資料庫
	@Column(name = "product_photo", columnDefinition = "longblob")
	private byte[] productPhoto;
	
	@ManyToOne
	@JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id")//name是我們的欄位，refer是我們參考別資料庫的欄位
	private ProductCategoryVO productCategoryVO;
	//	private Integer productCategoryId;
	
	@Column(name = "product_describtion", nullable = false)
	@NotEmpty(message="商品簡介: 請勿空白")
	private String productDescribtion;
	
	@Column(name = "product_price", nullable = false)
	@NotNull(message="商品價格: 請勿空白")
//	@Positive(message = "商品價格: 必須是正數")
	@Digits(integer = 10, fraction = 0)
	private Integer productPrice;
	
	@Column(name = "product_name", nullable = false)
	@NotEmpty(message="商品名稱: 請勿空白")
	private String productName;
	
	@Column(name = "product_status", nullable = false)
	private Boolean productStatus;
	
	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL)
	private Set<ShopOrderDetailVO> shopOrderDetailVO;
	


	public Set<ShopOrderDetailVO> getShopOrderDetailVO() {
		return shopOrderDetailVO;
	}

	public void setShopOrderDetailVO(Set<ShopOrderDetailVO> shopOrderDetailVO) {
		this.shopOrderDetailVO = shopOrderDetailVO;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
//	public Set<ProductPhotoVo> getProductPhotoVos() {
//		return ProductPhotoVos;
//	}
	

//	public void setProductPhotoVos(Set<ProductPhotoVo> productPhotoVos) {
//		ProductPhotoVos = productPhotoVos;
//	}

	public byte[] getProductPhoto() {
		return productPhoto;
	}

	public void setProductPhoto(byte[] productPhoto) {
		this.productPhoto = productPhoto;
	}

	public ProductCategoryVO getProductCategoryVO() {
		return productCategoryVO;
	}

	public void setProductCategoryVO(ProductCategoryVO productCategoryVO) {
		this.productCategoryVO = productCategoryVO;
	}

	public String getProductDescribtion() {
		return productDescribtion;
	}

	public void setProductDescribtion(String productDescribtion) {
		this.productDescribtion = productDescribtion;
	}

	public Integer getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Boolean getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Boolean productStatus) {
		this.productStatus = productStatus;
	}
//	
//	@Override
//	public String toString() {
//		return "Emp [productId=" + productId + ", productDescribtion=" + productDescribtion + "]";
//	}
	
	
	
}
