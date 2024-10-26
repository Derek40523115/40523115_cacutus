package com.member.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import com.activities_order.model.ActivityOrderVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notification.model.NotificationVO;
import com.productcomment.model.ProductCommentVO;
import com.shoporder.model.ShopOrderVO;

@Entity
@Table(name = "member")
public class MemberVO implements java.io.Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id" ,
		updatable = false, nullable = false)
	private Integer memberId;
	
	
	@Column(name = "member_email" ,
			nullable = false , unique = true)
	@Email
	@NotEmpty(message="會員信箱: 請勿空白")
	private String email;
	
	
	@Column(name = "member_name" , 
			nullable = false)
	@NotEmpty(message="會員名稱: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)]{2,10}$", message = "會員名稱: 只能是中、英文字母、數字,且長度必需在2到10之間")
	private String memberName;
	
	
	@Column(name = "member_password" , 
			nullable = false)
	@NotEmpty(message="會員密碼: 請勿空白")
	private String password;
	
	
	@Column(name = "member_birthday" , 
			nullable = false)
	@NotNull(message="會員生日: 請勿空白")
	@Past(message="日期必須是在今日(含)之前")
//	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;
	
	
	@Column(name = "member_gender" , 
			nullable = false)
//	@NotEmpty(message="會員性別: 請勿空白")
	private Integer gender;
	
	
	@Column(name = "member_phone" , 
			columnDefinition = "char",
			nullable = false)
	@NotEmpty(message="會員電話: 請勿空白")
	private String phone;
	
	
	@Column(name = "member_address" , 
			nullable = false)
	@NotEmpty(message="會員地址: 請勿空白")
	private String address;
	
	
	@Column(name = "member_pic" , 
			columnDefinition = "longblob")
	private byte[] memberPic;
	
	
	
	//Hibernate映射
	
	@OneToMany(mappedBy = "member" , cascade = CascadeType.ALL)
	private Set<NotificationVO> notificationVO;
	
//	@OneToMany(mappedBy = "member" , cascade = CascadeType.ALL)
//	private Set<RoomOrderVO> roomOrderVO;
	
	@OneToMany(mappedBy = "memberVO" , cascade = CascadeType.ALL)
	private Set<ActivityOrderVO> activityOrderVO;
	
	@OneToMany(mappedBy = "member" , cascade = CascadeType.ALL)
	private Set<ShopOrderVO> shopOrderVO;
	
	//商品評論
	@OneToMany(mappedBy="member", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<ProductCommentVO> productCommentVO;
		
	
	public Set<ProductCommentVO> getProductCommentVO() {
		return productCommentVO;
	}


	public void setProductCommentVO(Set<ProductCommentVO> productCommentVO) {
		this.productCommentVO = productCommentVO;
	}



	public Set<ShopOrderVO> getShopOrderVO() {
		return shopOrderVO;
	}



	public void setShopOrderVO(Set<ShopOrderVO> shopOrderVO) {
		this.shopOrderVO = shopOrderVO;
	}



	public MemberVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
		super();
		// TODO Auto-generated constructor stub
	}


	
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
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


	public byte[] getMemberPic() {
		return memberPic;
	}
	public void setMemberPic(byte[] memberPic) {
		this.memberPic = memberPic;
	}

	public Set<ActivityOrderVO> getActivityOrderVO() {
		return activityOrderVO;
	}

	public void setActivityOrderVO(Set<ActivityOrderVO> activityOrderVO) {
		this.activityOrderVO = activityOrderVO;
	}

	//非框架可直接做此改寫達到簡易轉換
//	public void setMemberPic(MultipartFile multipartfile) {
//		try {
//			this.memberPic = multipartfile.getBytes();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}



	public Set<NotificationVO> getNotificationVO() {
		return notificationVO;
	}


	public void setNotificationVO(Set<NotificationVO> notificationVO) {
		this.notificationVO = notificationVO;
	}




	
	
	
	
	
	//Override toString()
}
