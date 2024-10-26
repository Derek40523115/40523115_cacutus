package com.product.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.member.model.MemberVO;
import com.product.model.*;
import com.productcategory.model.ProductCategoryService;
import com.productcategory.model.ProductCategoryVO;
import com.productcomment.model.ProductCommentService;
import com.productcomment.model.ProductCommentVO;
import com.productphoto.model.ProductPhotoService;
import com.productphoto.model.ProductPhotoVo;

@Controller
@RequestMapping("/product")//網頁網址式/product/addProduct才會出現
public class ProductController {
	
	@Autowired
	ProductService productSvc;
	
	@Autowired
	ProductPhotoService productPhotoSvc;
	
	@Autowired
	ProductCategoryService ProductCategorySvc;
	
	@Autowired
	ProductCommentService productCommentSvc;
	
	@GetMapping("/listAllProductCategory")
	public String listAllProductCategory(Model model) {
		return "back_end/product/listAllProductCategory";
	}
	
	@GetMapping("/historyProduct")//我寫的
	public String historyProduct(Model model) {
		return "back_end/product/historyProduct";
	}
	
	@GetMapping("/select_page")
	public String select_page(Model model) {
		return "back_end/product/select_page";
	}

	@GetMapping("/shop_index")
	public String shop(Model model) {
		return "front_end/product/shop_index";
	}

	@GetMapping("/listAllProduct1")//我寫的
	public String listAllEmp(Model model) {
		return "back_end/product/listAllProduct";
	}


	//拿出所有商品
	@ModelAttribute("productListData") // for select_page.jsp 第96 108行用 // for listAllEmp.jsp 第68行用//ChatGpt鍵是empListData，值是list
		protected List<ProductVO> referenceListProductName(Model model) {
//	   	model.addAttribute("productVO", new ProductVO());
//	   	ProductService productSvc = new ProductService();
			List<ProductVO> list = productSvc.getAll();
			return list;
		}
	
	//是否顯示刪除欄位
	@ModelAttribute("productCategoryListDataPro")
	public boolean canDeleteAnyCategory(Model model) {
		List<ProductCategoryVO> categories = ProductCategorySvc.getAll(); // 獲取所有类别
	    for (ProductCategoryVO category : categories) {
	        if (category.getProductVOs() == null) {
	        	  model.addAttribute("productCategoryListDataPro", true);
	            return true; // 如果找到至少一个可以删除的类别，则返回true
	        }
	    }
	    model.addAttribute("productCategoryListDataPro", false);
	    return false; // 如果所有类别都不能删除，返回false
	}


	@ModelAttribute("productCategoryListData") // for select_page.jsp 第96 108行用 // for listAllEmp.jsp 第68行用//ChatGpt鍵是empListData，值是list
	protected List<ProductCategoryVO> referenceListData_productCategory(Model model) {
		model.addAttribute("productCategoryVO", new ProductCategoryVO());
//		ProductService productSvc = new ProductService();
		List<ProductCategoryVO> list = ProductCategorySvc.getAll();
		return list;
	}
	

	@GetMapping("addProduct")
	public String addEmp(ModelMap model) {
		ProductVO productVO = new ProductVO();
		model.addAttribute("productVO", productVO);
		return "back_end/product/addProduct";
	}
	
	@GetMapping("addProductCategory")
	public String addProductCategory(ModelMap model) {
		ProductCategoryVO productCategoryVO = new ProductCategoryVO();
		model.addAttribute("productCategoryVO", productCategoryVO);
		return "back_end/product/addProductCategory";
	}
	
	@PostMapping("insert")//對應到addEmp  56行 name名稱
	public String insert(@Valid ProductVO productVO, BindingResult result, ModelMap model,
			@RequestParam("productPhotoVos") MultipartFile[] parts) throws IOException {

		/***************************1.接收請求參數 - 輸入格式的錯誤處理******************/
		result = removeFieldError(productVO, result, "productPhotoVos");
		
		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			System.out.println("上傳照片");
			model.addAttribute("errorMessage", "商品照片: 請上傳照片");
		} else {
//			System.out.println("圖片新增");
			 Set<ProductPhotoVo> productPhotoVos = new HashSet<>(); // 用来存储ProductPhotoVo的集合
//			 Set<ProductPhotoVo> productPhotoVos = new LinkedHashSet<>(); // 用来存储ProductPhotoVo的集合
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				// 创建ProductPhotoVo对象，设置图片数据
	            ProductPhotoVo productPhotoVO = new ProductPhotoVo();
	            productPhotoVO.setProductVO(productVO); // 关联到ProductVO
	            productPhotoVO.setProductPhoto(buf); // 存储图片的字节数据
	            productPhotoVos.add(productPhotoVO); // 添加到集合
			}
			productVO.setProductPhotoVos(productPhotoVos); // 将图片集合设置到ProductVO
		}


		
		if (result.hasErrors() || parts[0].isEmpty()) {
//		    System.out.println("驗證錯誤：");
//		    for (FieldError error : result.getFieldErrors()) {
//		        System.out.println(error.getField() + ": " + error.getDefaultMessage());
//		    }
		    return "back_end/product/addProduct";
		}
		/***************************2.開始新增資料***************************************/
//		ProductService productSvc = new ProductService();
		productSvc.addProduct(productVO);
		/***************************3.新增完成,準備轉交(Send the Success view)***********/
		List<ProductVO> list = productSvc.getAll();
		model.addAttribute("productListData", list);
		model.addAttribute("success", "- (新增成功)");
//		System.out.println("成功新增");
		return "redirect:/product/listAllProduct1";//轉交給getMapping第76行
	}
	
	@PostMapping("insertCategory")
	public String insertCategory(@Valid ProductCategoryVO productCategoryVO, BindingResult result, ModelMap model) throws IOException {
		
		//重複類別驗證
		List<ProductCategoryVO> listCate = ProductCategorySvc.getAll();
		for(int i = 0 ; i < listCate.size() ; i++) {
			ProductCategoryVO cate = listCate.get(i);
			if(cate.getProductCategoryName().equals(productCategoryVO.getProductCategoryName())) {
//				model.addAttribute("error", "重複商品品項，請重新輸入");
				result.rejectValue("productCategoryName", "error.productCategoryName", "重複商品品項，請重新輸入");
				 return "back_end/product/addProductCategory";
			}
		}
		
		if (result.hasErrors()) {
		    System.out.println("驗證錯誤：");
		    for (FieldError error : result.getFieldErrors()) {
		        System.out.println(error.getField() + ": " + error.getDefaultMessage());
		    }
		    return "back_end/product/addProductCategory";
		}
		/***************************2.開始新增資料***************************************/
//		ProductService productSvc = new ProductService();
		ProductCategorySvc.addProductCategory(productCategoryVO);
		/***************************3.新增完成,準備轉交(Send the Success view)***********/
		List<ProductCategoryVO> list = ProductCategorySvc.getAll();
		model.addAttribute("productCategoryListData", list);
//		model.addAttribute("success", "- (新增成功)");
//		System.out.println("成功新增");
		return "redirect:/product/listAllProductCategory"; // 新增成功後轉交listAllProduct.jsp
	}
	
	//新增商品類別
		@PostMapping("addProductCategory")
		public String addProductCategory(@Valid ProductCategoryVO productCategoryVO, BindingResult result, ModelMap model) throws IOException {
			
			if (result.hasErrors()) {
//			    System.out.println("驗證錯誤：");
			    for (FieldError error : result.getFieldErrors()) {
			        System.out.println(error.getField() + ": " + error.getDefaultMessage());
			    }
			    return "back_end/product/listAllProductCategory";
			}
			
			ProductCategorySvc.addProductCategory(productCategoryVO);
			
			List<ProductCategoryVO> list = ProductCategorySvc.getAll();
			model.addAttribute("productCategoryListData", list);
			model.addAttribute("success", "- (新增成功)");
			System.out.println("成功新增");
			return "back_end/product/listAllProductCategory"; // 新增成功後轉交listAllProduct.jsp
		}
	
	
	/*
	 * This method will be called on listAllEmp.jsp form submission, handling POST request It also validates the user input
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("productId") String productId, ModelMap model) {
		/***************************1.接收請求參數 - 輸入格式的錯誤處理******************/
		/***************************2.開始查詢資料***************************************/
//		ProductService empSvc = new ProductService();
		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
		
		/***************************3.查詢完成,準備轉交(Send the Success view)***********/
		model.addAttribute("productVO", productVO);
//		model.addAttribute("getOne_For_Update", "true");
		
		return "back_end/product/update_product_input"; // 查詢完成後轉交update_product_input.jsp
//		return "back-end/product/listAllProduct";
	}
	
	//商品類別修改
	@PostMapping("getOneCategory_For_Update")
	public String getOneCategory_For_Update(@RequestParam("productCategoryId") String productCategoryId, ModelMap model) {
		/***************************1.接收請求參數 - 輸入格式的錯誤處理******************/
		/***************************2.開始查詢資料***************************************/
//		ProductService empSvc = new ProductService();
		System.out.println("getOneCategory_For_Update有執行");
		ProductCategoryVO productCategoryVO = ProductCategorySvc.getOneProductCategory(Integer.valueOf(productCategoryId));
		
		
		/***************************3.查詢完成,準備轉交(Send the Success view)***********/
		model.addAttribute("productCategoryVO", productCategoryVO);
//		model.addAttribute("getOne_For_Update", "true");
		
		return "back_end/product/update_productCategory_input"; // 查詢完成後轉交update_product_input.jsp
//		return "back-end/product/listAllProduct";
	}
	
	
	//下架商品
	@PostMapping("getOnehistory_For_Update")
	public String getOnehistory_For_Update(@RequestParam("productId") String productId, ModelMap model) {
		/***************************1.接收請求參數 - 輸入格式的錯誤處理******************/
		/***************************2.開始查詢資料***************************************/
//		ProductService empSvc = new ProductService();
		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
		
		/***************************3.查詢完成,準備轉交(Send the Success view)***********/
		model.addAttribute("productVO", productVO);
//		model.addAttribute("getOnehistory_For_Update", "true");
		
		return "back_end/product/update_product_input"; // 查詢完成後轉交update_product_input.jsp
//		return "back-end/product/listAllProduct";
	}
	
	
	/*
	 * This method will be called on update_emp_input.jsp form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ProductVO productVO, BindingResult result, ModelMap model,
			@RequestParam("productPhotos") MultipartFile[] parts,
			@RequestParam("productPhotoIds") Integer[] productPhotoIds
			) throws IOException {
		 // 使用者未選擇要上傳的新圖時
//		if(productVO.getProductPhoto().length==0) {
//			// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第151行
//			result = removeFieldError(productVO, result, "productPhoto");//把錯誤訊息給去掉,修改保持原圖也可以上傳
//	        // 從DB取出原upFiles的原byte[]置入empVO (用舊圖去update資料庫)
//	        ProductService empSvc = new ProductService();
////			byte[] upFiles = empSvc.getOneProduct(productVO.getProduct_id()).getProductPhoto();
////			productVO.setProductPhotobyte(upFiles);
////		}
		
		result = removeFieldError(productVO, result, "productPhoto");
//        if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
////        	byte[] upFiles = productSvc.getOneProduct(productVO.getProductId()).getProductPhoto();
////        	productVO.setProductPhoto(upFiles);
//		} else {
////			for (MultipartFile multipartFile : parts) {
////				byte[] upFiles = multipartFile.getBytes();
//////				productVO.setProductPhoto(upFiles);
////			}
//			
//			//貼上
////			System.out.println("圖片新增");
//			 Set<ProductPhotoVo> productPhotoVos = new HashSet<>(); // 用来存储ProductPhotoVo的集合
////			 Set<ProductPhotoVo> productPhotoVos = new LinkedHashSet<>(); // 用来存储ProductPhotoVo的集合
//			int i = 0;
//			 for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				// 创建ProductPhotoVo对象，设置图片数据
//	            ProductPhotoVo productPhotoVO = new ProductPhotoVo();
//	            
//	            productPhotoVO.setProductPhotoId(null);
//	            productPhotoVO.setProductVO(productVO); // 关联到ProductVO
//	            productPhotoVO.setProductPhoto(buf); // 存储图片的字节数据
//	            productPhotoVos.add(productPhotoVO); // 添加到集合
//			}
//			productVO.setProductPhotoVos(productPhotoVos); // 将图片集合设置到ProductVO
//			
//			
//		}
		
		//chatgpt
		Set<ProductPhotoVo> productPhotoVos = new HashSet<>();
	    
	    for (int i = 0; i < productPhotoIds.length; i++) {
	        Integer productPhotoId = productPhotoIds[i];
	        MultipartFile multipartFile = parts[i];
	        
	        ProductPhotoVo productPhotoVO = null;
	        if (productPhotoId != null && multipartFile.isEmpty()) {
	            // 圖片未修改，保留原有的 ProductPhotoVo
	            productPhotoVO = productPhotoSvc.getOneProduct(productPhotoId); // 從數據庫獲取現有的圖片數據
	        } else if(multipartFile != null && !multipartFile.isEmpty()) {
	            // 圖片有更新
	            productPhotoVO = new ProductPhotoVo();
	            productPhotoVO.setProductPhotoId(productPhotoId); // 使用原 ID 更新
	            productPhotoVO.setProductVO(productVO);
	            productPhotoVO.setProductPhoto(multipartFile.getBytes()); // 更新圖片數據
	        } 
	        else {
//	            continue; // 跳過無效情況
	        	System.out.println("有錯誤");
	        }
	        productPhotoVos.add(productPhotoVO);
	    }
	    productVO.setProductPhotoVos(productPhotoVos); // 設定圖片集合到 ProductVO
		//到這

		/***************************1.接收請求參數 - 輸入格式的錯誤處理******************/
		if (result.hasErrors()) {
			model.addAttribute("updatehistroy", "true");
			return "back_end/product/update_product_input";
		}
		/***************************2.開始修改資料***************************************/
//		ProductService productSvc = new ProductService();
		if(productVO.getProductStatus() == false) {
			model.addAttribute("updatehistroy", "true");
//			System.out.println("有到這裡");
		}
			
		productSvc.updateProduct(productVO);

		/***************************3.修改完成,準備轉交(Send the Success view)***********/
		model.addAttribute("success", "修改成功");
		productVO = productSvc.getOneProduct(Integer.valueOf(productVO.getProductId()));
		model.addAttribute("productVO", productVO);
		return "back_end/product/listOneProduct"; // 修改成功後轉交listOneEmp.jsp
		
	}
	
	//商品類別修改
		@PostMapping("updateCategory")
		public String update(@Valid ProductCategoryVO productCategoryVO, BindingResult result, ModelMap model) throws IOException {

//			result = removeFieldError(ProductCategoryVO, result, "productPhoto");
			
			/***************************1.接收請求參數 - 輸入格式的錯誤處理******************/
			if (result.hasErrors()) {
				return "back_end/product/update_productCategory_input";
			}
			/***************************2.開始修改資料***************************************/
//			ProductService productSvc = new ProductService();
			ProductCategorySvc.updateProductCategory(productCategoryVO);

			/***************************3.修改完成,準備轉交(Send the Success view)***********/
			model.addAttribute("success", "修改成功");
			return "redirect:/product/listAllProductCategory"; // 修改成功後轉交listOneEmp.jsp
		}
		
		@PostMapping("delete")
		public String delete(@RequestParam("productCategoryId") String productCategoryId, ModelMap model) {
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			/*************************** 2.開始刪除資料 *****************************************/
			// EmpService empSvc = new EmpService();
			ProductCategorySvc.deleteByProductCategory(Integer.valueOf(productCategoryId));
			/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
			List<ProductCategoryVO> list = ProductCategorySvc.getAll();
			model.addAttribute("productCategoryListData", list);
			model.addAttribute("success", "- (刪除成功)");
			return "back_end/product/listAllProductCategory"; // 刪除完成後轉交listAllEmp.html
		}
	
	
	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view.
	 *  如 : <form:select path="deptno" id="deptno" items="${mapData}" />
	 */
	@ModelAttribute("mapData") //
	protected Map<Boolean, String> referenceMapData() {
		Map<Boolean, String> mapData = new LinkedHashMap<Boolean, String>();
		mapData.put(true, "上架");
		mapData.put(false, "下架");
		return mapData;
	}
	
//	@ModelAttribute("productCategoryListData")
//	protected List<ProductCategoryVO> referenceListData() {
//		// DeptService deptSvc = new DeptService();
//		List<ProductCategoryVO> list = ProductCategorySvc.getAll();
//		return list;
//	}
	
	
	// 去除BindingResult中某個欄位的FieldError紀錄
		public BindingResult removeFieldError(ProductVO productVO, BindingResult result, String removedFieldname) {
			List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
					.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
					.collect(Collectors.toList());
			result = new BeanPropertyBindingResult(productVO, "productVO");
			for (FieldError fieldError : errorsListToKeep) {
				result.addError(fieldError);
			}
			return result;
		}
		
		/*
		 * This method will be called on select_page.html form submission, handling POST request
		 */
		@PostMapping("listProducts_ByCompositeQuery")
		public String listAllEmp(HttpServletRequest req, Model model) {
			Map<String, String[]> map = req.getParameterMap();
			List<ProductVO> list = productSvc.getAll(map);//關鍵行
			model.addAttribute("productListData", list); // for listAllEmp.html 第85行用
			return "back_end/product/historyProduct";
		}
		
		
		
		
		
		
		

		@GetMapping("/listAllProduct")
		public String listAllProduct(ModelMap model) {
			
			List<ProductVO> list1 = productSvc.getAll();
			model.addAttribute("productList", list1);
			
			List<ProductCategoryVO> list2 = ProductCategorySvc.getAll();
			model.addAttribute("productCategoryList", list2);
			return "/front_end/product/shop_index" ;
		}
		
		
		@GetMapping("/listOneProduct")
		public String listOneProduct(ModelMap model,
				@RequestParam("productId") String productId) {
			ProductVO productVO = productSvc.findById( Integer.valueOf(productId) );
			model.addAttribute("productVO", productVO);
			return "/front_end/product/shop_single" ;
		}
		
		
		
		
	/*******************************************************************/
		//方法及別驗證
		@ExceptionHandler(value = { ConstraintViolationException.class })
		//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
		public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
		    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		    StringBuilder strBuilder = new StringBuilder();
		    for (ConstraintViolation<?> violation : violations ) {
		          strBuilder.append(violation.getMessage() + "<br>");
		    }
		    //==== 以下第80~85行是當前面第69行返回 /src/main/resources/templates/back-end/emp/select_page.html 第97行 與 第109行 用的 ====   
//		    model.addAttribute("empVO", new EmpVO());
//	    	EmpService empSvc = new EmpService();
			List<ProductVO> list = productSvc.getAll();
			model.addAttribute("empListData", list); // for select_page.html 第97 109行用
			
			String message = strBuilder.toString();
		    return new ModelAndView("back-end/emp/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
		}
		
//		@GetMapping("/listAllTest")
//		public String listAllTest(ModelMap model) {
//			List<ProductVO> list = productSvc.getAll();
//			model.addAttribute("productList", list);
//			return "/front_end/product/shop_indexORI";
//		}
//
//	    @GetMapping("/listOneTest/{productId}")
//	    public String listOneTest(@PathVariable Integer productId, ModelMap model) {
//	    	System.out.println("aaa");
//	        ProductVO productVO = productSvc.findById(productId);
//	        model.addAttribute("productVO", productVO);
//	        return "/front_end/product/shop_singleORI";
//	    }
		
		
		//新的商店前端網頁
				@GetMapping("/listAllProduct2")
				public String listAllProduct2(ModelMap model) {
					List<ProductVO> list1 = productSvc.getAll();
					model.addAttribute("productList", list1);
					
					List<ProductCategoryVO> list2 = ProductCategorySvc.getAll();
					model.addAttribute("productCategoryList", list2);
					
					Map<Integer,Integer> productFirstPhotoId = new HashMap<>();
					Iterator<ProductVO> iterProduct = list1.iterator();
					while(iterProduct.hasNext()) {
						ProductVO perProduct = iterProduct.next();//抓陣列裡單個產品的資料
						
						Set<ProductPhotoVo> productPhotoVos = perProduct.getProductPhotoVos();
						if(productPhotoVos!=null && !productPhotoVos.isEmpty()) {
							ProductPhotoVo firstPhotoId = productPhotoVos.iterator().next();
							productFirstPhotoId.put(perProduct.getProductId(), firstPhotoId.getProductPhotoId());
						}
					}

					
					model.addAttribute("productFirstPhotoId", productFirstPhotoId);
					return "/front_end/product/shop" ;
				}
				
				@GetMapping("/listOneProduct2")
				public String listOneProduct2(@RequestParam("productId") String productId, ModelMap model, HttpSession session) {
					ProductVO productVO = productSvc.findById( Integer.valueOf(productId) );
					//抓多張圖片的ID
					Set<ProductPhotoVo> productPhotoVos = productVO.getProductPhotoVos();
					Set<Integer> productPhotoId = new LinkedHashSet();
					for(ProductPhotoVo productPhotoVo : productPhotoVos) {
						productPhotoId.add(productPhotoVo.getProductPhotoId());
					}
					model.addAttribute("productPhotoId", productPhotoId);
					model.addAttribute("productVO", productVO);
					
					//從相同商品類別裡找多種商品
					Integer productCategoryId = productVO.getProductCategoryVO().getProductCategoryId();
					ProductCategoryVO productCategoryVO = ProductCategorySvc.getOneProductCategory(productCategoryId);
					Set<ProductVO> productVOs = productCategoryVO.getProductVOs();

					// 使用 Stream 限制 Set 中的元素數量為前四個
					Set<ProductVO> limitedProductVOs = productVOs.stream().limit(4).collect(Collectors.toSet());

					System.out.println("限制後的商品集合: " + limitedProductVOs);
					model.addAttribute("limitedProductVOs", limitedProductVOs);

					
					
					//商品評論
					List<ProductCommentVO> productCommentVO = productCommentSvc.getProductIdAll(productId);
//					model.addAttribute("productCommentVO", productCommentVO);
//					System.out.println("測試檔案 = " +productCommentVO);
					
					//會員
					MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");
					model.addAttribute("memberVO", memberVO);
					if(memberVO != null) {//登入會員
					
						  // 當前用戶已登入，提取出用戶自己的評論
					    List<ProductCommentVO> otherComments = productCommentVO.stream()
					        .filter(comment -> !comment.getMember().getMemberId().equals(memberVO.getMemberId()))
					        .collect(Collectors.toList());

					    // 找出用戶自己的評論
					    ProductCommentVO myComment = productCommentVO.stream()
					        .filter(comment -> comment.getMember().getMemberId().equals(memberVO.getMemberId()))
					        .findFirst().orElse(null);
					    
					    // 將用戶自己的評論和其他評論分別傳遞到前端
					    model.addAttribute("myComment", myComment);
					    model.addAttribute("otherComments", otherComments);

							
					}else {
						// 用戶未登入，傳遞所有評論
					    model.addAttribute("allComments", productCommentVO);
					}
					
					
					return "/front_end/product/detail";
				}
		
		
}
