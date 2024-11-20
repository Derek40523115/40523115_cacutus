package com.productpromotion.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "shop_discount_project")
public class ShopDiscountProjectVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "promotion_id")
	private Integer promotionId;
	
	@NotNull(message = "測試")
	@NotEmpty(message = "促銷標題:請勿空白")
	@Column(name = "promotion_title", nullable = false)
	private String promotionTitle;
	
	@NotEmpty(message = "促銷敘述:請勿空白")
	@Column(name = "promotion_content", nullable = false)
	private String promotionContent;
	
	@Column(name = "promotion_state", nullable = false)
	private Boolean promotionState;
	
//	@NotEmpty(message = "請選擇折價")
	@Column(name = "promotion_coupon", nullable = false)
	private Double promotionCoupon;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(message = "請選擇開始時間")
	@Column(name = "promotion_started", nullable = false)
//	private Timestamp promotionStarted;
	private LocalDate promotionStarted;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(message = "請選擇促銷結束時間")
	@Column(name = "promotion_end", nullable = false)
//	private Timestamp promotionEnd;
	private LocalDate promotionEnd;

	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionTitle() {
		return promotionTitle;
	}

	public void setPromotionTitle(String promotionTitle) {
		this.promotionTitle = promotionTitle;
	}

	public String getPromotionContent() {
		return promotionContent;
	}

	public void setPromotionContent(String promotionContent) {
		this.promotionContent = promotionContent;
	}

	public Boolean getPromotionState() {
		return promotionState;
	}

	public void setPromotionState(Boolean promotionState) {
		this.promotionState = promotionState;
	}

	public Double getPromotionCoupon() {
		return promotionCoupon;
	}

	public void setPromotionCoupon(Double promotionCoupon) {
		this.promotionCoupon = promotionCoupon;
	}

//	public Timestamp getPromotionStarted() {
//		return promotionStarted;
//	}
	public LocalDate getPromotionStarted() {
		return promotionStarted;
	}

	public void setPromotionStarted(LocalDate promotionStarted) {
		this.promotionStarted = promotionStarted;
	}

//	public Timestamp getPromotionEnd() {
//		return promotionEnd;
//	}
	
	public LocalDate getPromotionEnd() {
		return promotionEnd;
	}

	public void setPromotionEnd(LocalDate promotionEnd) {
		this.promotionEnd = promotionEnd;
	}
	
	
}
