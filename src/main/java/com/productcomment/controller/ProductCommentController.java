package com.productcomment.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.member.model.MemberVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.productcomment.model.ProductCommentService;
import com.productcomment.model.ProductCommentVO;

@Controller
@RequestMapping("/product")
public class ProductCommentController {
	
	@Autowired
	private ProductService productService;  // 你需要创建一个 service 或者 repository 来查询 Product

	
	@Autowired
	private ProductCommentService productCommentService;
	
//	@Autowired
	
	@PostMapping("/addProductComment")
	@ResponseBody
	public Integer addProductComment(@RequestBody Map<String, Object> data, HttpSession session) {
	    // 获取前端传递的 productId, comment 和 star
	    Integer productId = (Integer) data.get("productId");
	    String comment = (String) data.get("productComment");
	    Integer star = (Integer) data.get("star");
//	    Integer memberId = (Integer)data.get("memberId");
	    
//	    System.out.println("確認回傳訊息 = "+productId);
	    // 通过 productId 查找对应的 ProductVO 对象
	    ProductVO product = productService.findById(productId);

//	    HttpSession session = req.getSession();
	    MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");
//	    Integer memberId = memberVO.getMemberId();
//	    System.out.println("確認有收到VO = "+memberVO);
	    // 创建并设置 ProductCommentVO 对象
	    ProductCommentVO productCommentVO = new ProductCommentVO();
	    productCommentVO.setProductComment(comment);
	    productCommentVO.setStar(star);
	    productCommentVO.setProduct(product);  // 这里设置完整的 ProductVO 对象
	    productCommentVO.setMember(memberVO);
	    
	    // 调用服务层保存评论
	    productCommentService.addProductComment(productCommentVO);
	    
	    Integer productCommentId = productCommentVO.getProductCommentId();
//	    System.out.println("主鍵值 = " + productCommentId);

	    return productCommentId;
	}
	
	@ResponseBody
	@PostMapping("/updateProductComment")
	public String updateProductComment(@RequestBody Map<String, Object> data, HttpSession session) {
		// 获取前端传递的 productId, comment 和 star
	    Integer productId = (Integer) data.get("productId");
	    String comment = (String) data.get("productComment");
	    Integer star = (Integer) data.get("star");
	    Integer productCommemntId = (Integer) data.get("productCommemntId");
//	    Integer memberId = (Integer)data.get("memberId");
	    
//	    System.out.println("確認回傳訊息 = "+productId);
	    // 通过 productId 查找对应的 ProductVO 对象
	    ProductVO product = productService.findById(productId);

//	    HttpSession session = req.getSession();
	    MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");
//	    Integer memberId = memberVO.getMemberId();
//	    System.out.println("確認有收到VO = "+memberVO);
	    // 创建并设置 ProductCommentVO 对象
	    ProductCommentVO productCommentVO = new ProductCommentVO();
	    productCommentVO.setProductComment(comment);
	    productCommentVO.setStar(star);
	    productCommentVO.setProduct(product);  // 这里设置完整的 ProductVO 对象
	    productCommentVO.setMember(memberVO);
	    productCommentVO.setProductCommentId(productCommemntId);
		
	    //修改資料庫
	    productCommentService.updateProductComment(productCommentVO);
	    
		return "成功修改";
	}
	
	@ResponseBody
	@PostMapping("/deleteProductComment")
	public String deleteProductComment(@RequestBody Integer productCommentId) {
		productCommentService.deleteProductComment(productCommentId);
		return "成功刪除";
	}

	
	
	
//	@PostMapping("insert")
//	public String addProductComment(@RequestBody ProductCommentVO productCommentVO,ModelMap model) {
////		ProductCommentVO productCommentVO = new ProductCommentVO();
////		model.addAttribute("productCommentVO", productCommentVO);
//		return"成功加入";
//	}
	
	
//	@PostMapping("/addProductComment")
//	public String addProductComment(@RequestBody ProductCommentVO productCommentVO, ModelMap model) {
//	    // 你可以通過 productCommentVO.getProduct() 來獲取 ProductVO
//	    ProductVO product = productCommentVO.getProduct();
//	    if (product != null) {
//	        Integer productId = product.getProductId();
//	        // 可以通過 productId 進行進一步的邏輯處理
//	    }
//	    System.out.println("成功執行");
//
//	    return "成功加入";
//	}
	
	
//	@PostMapping("/addProductComment")
//	public ResponseEntity<String> addProductComment(@RequestBody ProductCommentVO productCommentVO) {
//	    ProductVO product = productCommentVO.getProduct();
//	    
//	    if (product != null) {
//	        Integer productId = product.getProductId();
//	        // 可以通過 productId 進行進一步的邏輯處理
//	        System.out.println("Product ID: " + productId);
//	    }
//
//	    System.out.println("成功執行");
//	    return new ResponseEntity<>("成功加入", HttpStatus.OK);
//	}

	
	
}
