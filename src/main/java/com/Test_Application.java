//package com;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import com.member.model.MemberVO;
//import com.product.model.ProductVO;
//import com.productcomment.model.ProductCommentRepository;
//import com.productcomment.model.ProductCommentVO;
//
//@SpringBootApplication
//public class Test_Application implements CommandLineRunner{
//	
//	@Autowired
//	ProductCommentRepository repository;
//	
//	public static void main(String[] args) {
//		SpringApplication.run(Test_Application.class);
//	}
//	
//	
//	@Override
//	public void run(String...args) throws Exception{
//		MemberVO memberVO = new MemberVO();
//		memberVO.setMemberId(1);
//		
//		ProductVO productVO = new ProductVO();
//		productVO.setProductId(19);
//		
//		ProductCommentVO productCommentVO = new ProductCommentVO();
//		productCommentVO.setMember(memberVO);
//		productCommentVO.setProduct(productVO);
//		productCommentVO.setProductComment("此商品太好用了五星好評");
//		repository.save(productCommentVO);
//	}
//}
