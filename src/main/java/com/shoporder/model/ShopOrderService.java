package com.shoporder.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cart.model.Cart;
import com.cart.model.CartService;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Component
public class ShopOrderService {
	
	@Autowired
	ShopOrderRepository repository;
	
	@Autowired
	CartService cartSvc;
	
	public ShopOrderVO addOrder(ShopOrderVO shopOrderVO) {
		return repository.save(shopOrderVO);
	}
	
	public void updateOrder(ShopOrderVO shopOrderVO) {
		repository.save(shopOrderVO);
	}
	
	public List<ShopOrderVO> getAll() {
		return repository.findAll();
	}
	
	public void deleteOrder(Integer shopOrderId) {
		repository.deleteById(shopOrderId);
	}
	
    public Optional<ShopOrderVO> findById(Integer orderId) {
    	return repository.findById(orderId); 
    }

	
	public List<ShopOrderVO> findByMemberId(Integer memberId){
		return repository.findByMemberId(memberId);
	}
	
	
	//綠界金流 4311-9522-2222-2222
	public String ecpayCheckout(Integer productOrderId) {
		
//		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
			
		AllInOne all = new AllInOne("");	
		AioCheckOutALL obj = new AioCheckOutALL();
		
		ShopOrderVO shopOrderVO = repository.findById(productOrderId).get();
		
		//將訂單時間(當前時間)轉為字串放入，以避免編號重複的問題
		Date date = new Date();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String orderDateString = formatter1.format(date);		
		
		
		//訂單編號均為唯一值不可重複使用,可使用大小寫英文和數字混合避免重複,限20字內
		obj.setMerchantTradeNo( orderDateString + "OID" +productOrderId  );
		
		//訂單時間(yyyy/MM/dd HH:mm:ss)
    	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		obj.setMerchantTradeDate(formatter2.format(date).toString());
		
		//訂單總價(帶整數，不可有小數點)
		obj.setTotalAmount(shopOrderVO.getProductTotal().toString());
		
		//描述(勿帶入特殊字元)
		obj.setTradeDesc("test Description");
		
		//商品項目名稱(若商品名稱有多筆需在金流選擇頁一行一行顯示名稱，商品名稱請以符號#分隔-總字數限制中英數400字)
		
		Integer memberId = shopOrderVO.getMember().getMemberId();
		List<Cart> cartList = cartSvc.findAllItem(memberId);
		
		String allProductDetail = "";
		//旗標,檢視是否為購物車中最後一個物件
		int count = 0;
		
		for(Cart cart : cartList) {
			count++;
			if(count < cartList.size()) {
				String singleProductDetail = new StringBuilder(cart.getProductName())
														.append(cart.getQuantity().toString())
														.append("X")
														.append(cart.getPrice().toString())
														.append("=$")
														.append( (cart.getQuantity()*cart.getPrice()) )
														.toString();
				allProductDetail += singleProductDetail + "#";
			}else {
				String singleProductDetail = new StringBuilder(cart.getProductName())
														.append(cart.getQuantity().toString())
														.append("X")
														.append(cart.getPrice().toString())
														.append("=$")
														.append( (cart.getQuantity()*cart.getPrice()) )
														.toString();
				allProductDetail += singleProductDetail;
			}
		}
		System.out.println(allProductDetail);
		obj.setItemName(allProductDetail);

		
		//付款完成通知回傳網址(為付款結果通知回傳網址,為server或主機的URL)
		obj.setReturnURL("http://211.23.128.214:5000");
		
		//返回網址(消費者點選此按鈕後,會將頁面導回到此設定的網址,須為外網網址)
//		obj.setClientBackURL("http://cactus-resort.ddns.net");
		obj.setClientBackURL("http://localhost:8080");
		
		
		obj.setNeedExtraPaidInfo("N");
		
		
		String form = all.aioCheckOut(obj, null);
		
			
		return form;
	}
}
