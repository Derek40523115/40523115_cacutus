	package com.productphoto.model;
	
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.JoinColumn;
	import javax.persistence.ManyToOne;
	import javax.persistence.Table;
	
	import com.product.model.ProductVO;
	
	@Entity
	@Table(name="product_photo")
	public class ProductPhotoVo {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "product_photo_id", updatable = false)
		private Integer productPhotoId;
		
		@ManyToOne
		@JoinColumn(name = "product_id", referencedColumnName = "product_id")//name是我們的欄位，refer是我們參考別資料庫的欄位
		private ProductVO productVO;
		
		//	private Integer productId;
		
		@Column(name = "product_photo", columnDefinition = "longblob")
		private byte[] productPhoto;
		
		
		public Integer getProductPhotoId() {
			return productPhotoId;
		}
		public void setProductPhotoId(Integer productPhotoId) {
			this.productPhotoId = productPhotoId;
		}
			
		public ProductVO getProductVO() {
			return productVO;
		}
		//ProductPhotoVO裡有ProductVO的物件且裡面裝有ProductVO的相關資訊
		//所以可以從ProductPhotoVO裡拿ProductVO的東西
		public void setProductVO(ProductVO productVO) {
			this.productVO = productVO;
		}
		public byte[] getProductPhoto() {
			return productPhoto;
		}
		public void setProductPhoto(byte[] productPhoto) {
			this.productPhoto = productPhoto;
		}
		
		
		
		
	}
