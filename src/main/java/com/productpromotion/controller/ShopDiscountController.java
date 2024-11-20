package com.productpromotion.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.productpromotion.model.ShopDiscountProjectVO;
import com.productpromotion.model.ShopDiscountService;

@Controller
@RequestMapping("/productDiscount")
public class ShopDiscountController {

	@Autowired
	ShopDiscountService shopDiscountService;
	
	@GetMapping("/addProductDiscount")
	public String addProductDiscount(Model model){
		ShopDiscountProjectVO shopDiscountProjectVO = new ShopDiscountProjectVO();
		model.addAttribute("shopDiscountProjectVO", shopDiscountProjectVO);
		return "back_end/productDiscount/addProductDiscount";
	}
	
	@PostMapping("insert")
	public String insert(@Valid ShopDiscountProjectVO shopDiscountProjectVO, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) throws IOException{
		if(result.hasErrors()) {
//			model.addAttribute("ShopDiscountProjectVO", shopDiscountProjectVO);
			return "back_end/productDiscount/addProductDiscount";
		}
//		System.out.println("開始日期 = " + shopDiscountProjectVO.getPromotionStarted());
//		System.out.println("結束日期 = " + shopDiscountProjectVO.getPromotionEnd());
		shopDiscountService.addShopDiscount(shopDiscountProjectVO);
//		System.out.println("執行測試");
		List<ShopDiscountProjectVO> shopProductDiscount = shopDiscountService.getAll();
		model.addAttribute("shopProductDiscount", shopProductDiscount);
		return "/back_end/productDiscount/showAllProductDiscount";
	}
	
	
	@GetMapping("/getAllProductDiscount")
	public String getAll(Model model) {
		List<ShopDiscountProjectVO> shopProductDiscount = shopDiscountService.getAll();
		model.addAttribute("shopProductDiscount", shopProductDiscount);
		return "back_end/productDiscount/showAllProductDiscount";
	}
	
	@PostMapping("/getDiscount_For_Update")
	public String getDiscount_For_Update(ModelMap model,@RequestParam("promotionId") String promotionId) {
		ShopDiscountProjectVO shopDiscountProjectVO = shopDiscountService.getOneDiscount(Integer.valueOf(promotionId));
		model.addAttribute("shopDiscountProjectVO", shopDiscountProjectVO);
		return "/back_end/productDiscount/updateProductDiscount";
	}
	
	@PostMapping("update")
	public String update(@Valid ShopDiscountProjectVO shopDiscountProjectVO,BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "/back_end/productDiscount/updateProductDiscount";
		}
		shopDiscountService.updateShopDiscount(shopDiscountProjectVO);
		
		List<ShopDiscountProjectVO> shopProductDiscount = shopDiscountService.getAll();
		model.addAttribute("shopProductDiscount", shopProductDiscount);
		//傳送單個資料
//		shopDiscountProjectVO = shopDiscountService.getOneDiscount(Integer.valueOf(shopDiscountProjectVO.getPromotionId()));
//		model.addAttribute("shopProductDiscount", shopDiscountProjectVO);
		return "/back_end/productDiscount/showAllProductDiscount";
	}
	
}
