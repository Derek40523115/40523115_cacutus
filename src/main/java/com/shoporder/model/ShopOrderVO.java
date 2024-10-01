package com.shoporder.model;

import java.sql.Timestamp;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.member.model.MemberVO;
import com.shoporderdetail.model.ShopOrderDetailVO;

@Entity
@Table(name = "shop_order")
public class ShopOrderVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shop_order_id")
	private Integer shopOrderId;
	
	@Column(name = "product_order_date"
			,insertable = false, updatable = false, nullable = false)
	private Timestamp productOrderDate;
	
	@NotNull
	@Column(name = "product_total", nullable = false)
	private Integer productTotal;
	
	
	@NotNull
	@Column(name = "order_status", nullable = false)
	private Integer orderStatus;
	
	
	@NotNull
	@Column(name = "customer_name", nullable = false)
	private String name;
	
	@NotNull
	@Column(name = "customer_email", nullable = false)
	private String email;
	
	@NotNull
	@Column(name = "customer_phone", nullable = false)
	private String phone;
	
	@NotNull
	@Column(name = "customer_address", nullable = false)
	private String address;
	
	
	@Transient
	private String promotion;
	
	
	
	
	
	
	
	
	
	//映射
	
	@OneToMany(mappedBy = "shopOrder" , cascade = CascadeType.ALL)
	private Set<ShopOrderDetailVO> shopOrderDetailVO;
	
	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "member_id")//name是我們的欄位，refer是我們參考別資料庫的欄位
	private MemberVO member;

	public Integer getShopOrderId() {
		return shopOrderId;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}

	public Timestamp getProductOrderDate() {
		return productOrderDate;
	}

	public void setProductOrderDate(Timestamp productOrderDate) {
		this.productOrderDate = productOrderDate;
	}

	public Integer getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(Integer productTotal) {
		this.productTotal = productTotal;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Set<ShopOrderDetailVO> getShopOrderDetailVO() {
		return shopOrderDetailVO;
	}

	public void setShopOrderDetailVO(Set<ShopOrderDetailVO> shopOrderDetailVO) {
		this.shopOrderDetailVO = shopOrderDetailVO;
	}

	public MemberVO getMember() {
		return member;
	}

	public void setMember(MemberVO member) {
		this.member = member;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	
	
}
