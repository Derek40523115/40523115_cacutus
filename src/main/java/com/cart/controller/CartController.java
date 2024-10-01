package com.cart.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cart.model.Cart;
import com.cart.model.CartService;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.notification.model.NotificationService;
import com.product.model.ProductServiceImpl;
import com.product.model.ProductVO;
import com.productpromotion.model.ShopDiscountProjectVO;
import com.productpromotion.model.ShopDiscountService;
import com.shoporder.model.ShopOrderService;
import com.shoporder.model.ShopOrderVO;
import com.shoporderdetail.model.ShopOrderDetailService;
import com.shoporderdetail.model.ShopOrderDetailVO;

@Controller
//@Validated
@RequestMapping("/shopCart")
public class CartController {
	 //由shop_cart點擊下單後進入此controller 151行
    @Autowired
    CartService cartSvc;

    @Autowired
    ShopOrderService shopOrderSvc;

    @Autowired
    ShopOrderDetailService shopOrderDetailSvc;

    @Autowired
    ProductServiceImpl productSvc;

    @Autowired
    MemberService memSvc;

    @Autowired
    NotificationService notiSvc;
    
    @Autowired
    ShopDiscountService shopDiscountService;

    @ResponseBody
    @PostMapping("/addOneToCart")
    public String addOneToCart(@RequestBody Cart cart, HttpSession session) {

        //從Session中取得會員資料
        String email = (String) session.getAttribute("account");
        Integer memberId = memSvc.findByEmail(email).getMemberId();

        //找出對應商品
        ProductVO productVO = productSvc.findById(cart.getProductId());
        cart.setPrice(productVO.getProductPrice());
        cart.setProductName(productVO.getProductName());

        // 將一筆購物項目(訂單明細DTO)和會員ID放進購物車
        return cartSvc.addToCart(cart, memberId);
    }

    // 導入購物車頁面
    @GetMapping("/shopCart")
    public String shopCart(ModelMap model,HttpSession session) {
    	//活動折扣
    	LocalDate today = LocalDate.now();
    	List<ShopDiscountProjectVO> shopProductDiscount = shopDiscountService.usePromotion(today);
//    	System.out.println("測試 = " + shopProductDiscount);
		model.addAttribute("shopProductDiscount", shopProductDiscount);
//    	session.setAttribute("shopProductDiscount", shopProductDiscount);
		//到這
        return "front_end/product/shop_cart";
    }

    // 前端傳來要找該會員的購物車
    @ResponseBody
    @GetMapping("/shopCartByMember")
    public List<Cart> findShopCart(HttpSession session) {

        //從Session中取得會員資料
        String email = (String) session.getAttribute("account");
        Integer memberId = memSvc.findByEmail(email).getMemberId();

        return cartSvc.findAllItem(memberId);
    }

    @ResponseBody
    @PostMapping("/upDateCart")
    public String upDateCart(HttpSession session, @RequestBody Cart cart) {

        //從Session中取得會員資料
        String email = (String) session.getAttribute("account");
        Integer memberId = memSvc.findByEmail(email).getMemberId();

        if (memberId != null) {
//			System.out.println("開始");
            //找出對應商品
            ProductVO productVO = productSvc.findById(cart.getProductId());
            cart.setPrice(productVO.getProductPrice());
            cart.setProductName(productVO.getProductName());

            cartSvc.updateOneItem(memberId, cart);
//			System.out.println("結束");
        } else {
            return "沒找到Id";
        }
        return "修改成功";
    }

    @ResponseBody
    @PostMapping("/removeOneCartItem")
    public String removeOneCartItem(@RequestBody Map<String, Object> requestData, HttpSession session) {
        Integer productId = (Integer) requestData.get("productId");

        //從Session中取得會員資料
        String email = (String) session.getAttribute("account");
        Integer memberId = memSvc.findByEmail(email).getMemberId();

        if (memberId != null) {

//			System.out.println("開始");
            cartSvc.removeOneItem(productId, memberId);
//			System.out.println("結束");

        } else {
            return "沒找到Id";
        }

        return "移除成功";
    }
    //由shop_cart點擊下單後進入此controller
    @GetMapping("/CartToCheckout")
    public String cartToCheckout(HttpSession session,ModelMap model,@RequestParam("productPromotion") String productPromotion) {
        MemberVO memberVO = memSvc.findByEmail((String)session.getAttribute("account"));
        model.addAttribute("memberVO", memberVO);
//        System.out.println("測試 = " + productPromotion);
        model.addAttribute("productPromotion", productPromotion);
        
        
        return "front_end/product/shop_checkout";
    }
    //進入綠界
    @PostMapping("/checkoutOrder")
    public String checkoutOrder(ShopOrderVO shopOrderVO, HttpSession session,ModelMap model,@RequestParam("productPromotion") String productPromotion) {

        //從Session中取得會員資料
        String email = (String) session.getAttribute("account");
        Integer memberId = memSvc.findByEmail(email).getMemberId();

        if (memberId != null) {
            MemberVO memberVO = memSvc.findByPK(memberId);
            List<Cart> cart = cartSvc.findAllItem(memberId);

            shopOrderVO.setMember(memberVO);
            shopOrderVO.setOrderStatus(1);
            shopOrderVO.setPromotion(productPromotion);

            //先將訂單做新增
            shopOrderSvc.addOrder(shopOrderVO);
//			System.out.println("訂單新增成功");



            //成功後再新增訂單明細資料
//			Integer count = 0;
            if (cart != null) {
                for (Cart item : cart) {
                    ShopOrderDetailVO shopOrderDetailVO = new ShopOrderDetailVO();

                    ProductVO productVO = productSvc.findById(item.getProductId());
                    shopOrderDetailVO.setShopOrder(shopOrderVO);
                    shopOrderDetailVO.setProduct(productVO);
                    shopOrderDetailVO.setOrderQuantity(item.getQuantity());
                    shopOrderDetailVO.setProductAmount( (int)((item.getQuantity()) * (item.getPrice()) * (Double.valueOf(productPromotion))) );

                    shopOrderDetailSvc.addShopOrderDetail(shopOrderDetailVO);

//					System.out.println("明細新增success");

//					count++;

                }
            }

//			System.out.println("訂單明細資料共新增"+count+"筆");

            Date date = new Date();
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = formatter1.format(date);

            //發送通知給會員
            notiSvc.orderSuccess(memberId, 2,
                    "親愛的"+ memberVO.getMemberName() +"，您好，您的訂單(編號:"+shopOrderVO.getShopOrderId()+")已於"+nowTime+"成功成立，非常感謝您的支持!!");

//			System.out.println("message has send");
            
//			// 綠界串流
            String ecpayCheckout = shopOrderSvc.ecpayCheckout(shopOrderVO.getShopOrderId());
            model.addAttribute("ecpayCheckout", ecpayCheckout);

            cartSvc.cleanAllCart(memberId);
        }
        return "front_end/product/success";
    }



	@ResponseBody
	@GetMapping("/cleanShopCart")
	public String cleanShopCart(HttpSession session) {
		
		//從Session中取得會員資料
		String email = (String) session.getAttribute("account");
		Integer memberId = memSvc.findByEmail(email).getMemberId();	
		
		if (memberId != null) {
			cartSvc.cleanAllCart(memberId);
		}
		return "/product/listAllProduct";
	}
	
	@ResponseBody
	@GetMapping("/cartTotalNumber")
	public Integer cartTotalNumber(HttpSession session) {
		
		String email = (String)session.getAttribute("account");
//		
//		System.out.println("aaaaaaa");
		
		if(email == null) {
			return 0;
		}else {
			MemberVO memberVO = memSvc.findByEmail(email);
			Integer memberId = memberVO.getMemberId();
			return cartSvc.getCartNumber(memberId);	
		}		
	}
	
}
