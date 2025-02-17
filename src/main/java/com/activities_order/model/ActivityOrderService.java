package com.activities_order.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.activities_item.model.ItemRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class ActivityOrderService {

    @Autowired
    ActivityOrderRepository activityOrderRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ItemRepository itemRepository;

    public void addOrder(ActivityOrderVO activityOrderVO){
        activityOrderRepository.save(activityOrderVO);
    }

    public void updateOrder(ActivityOrderVO activityOrderVO){
        activityOrderRepository.save(activityOrderVO);
    }

    public void deleteOrder(Integer activityOrderId){
        if(activityOrderRepository.existsById(activityOrderId))
            activityOrderRepository.deleteById(activityOrderId);
    }

    public ActivityOrderVO getOneOrder(Integer activityOrderId){
        Optional<ActivityOrderVO> optional = activityOrderRepository.findById(activityOrderId);
        return optional.orElse(null);// public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
    }

//    public ActivityOrderVO getOneOrder(Integer activityOrderId){
//        Optional<ActivityOrderVO> optional = activityOrderRepository.findById(activityOrderId);
//        ActivityOrderVO activityOrderVO = optional.orElse(null);
//        if (activityOrderVO != null) {
//            updateOrderStateIfExpired(activityOrderVO);
//        }
//        return activityOrderVO;
//    }

//    public List<ActivityOrderVO> getAll(){
//
//        return activityOrderRepository.findAll();
//    }
    public List<ActivityOrderVO> getAll() {
        List<ActivityOrderVO> list = activityOrderRepository.findAll();
        list.forEach(this::updateOrderStateIfExpired);
        return list;
    }

    private void updateOrderStateIfExpired(ActivityOrderVO activityOrderVO) {
        java.sql.Date orderTime = activityOrderVO.getSessionVO().getActivityDate();
        LocalDate orderDate = convertToLocalDate(orderTime);
        LocalDate currentDate = LocalDate.now();

        // 如果現在的日期超過場次日期一天並且狀態為0
        if (currentDate.isAfter(orderDate.plusDays(0)) && activityOrderVO.getOrderState() == 0) {
            activityOrderVO.setOrderState(2);
            activityOrderRepository.save(activityOrderVO); // 更新DB中的狀態
        }
    }

    private LocalDate convertToLocalDate(java.sql.Date dateToConvert) {
        return dateToConvert.toLocalDate();
    }

    public List<ActivityOrderVO> getOrderTimeBetween(Date start, Date end){
        return activityOrderRepository.findByOrderTimeBetween(start, end);
    }

    public List<ActivityOrderVO> getAll(Map<String, String[]> map){
        return ActivityOrder_Compositegory.getAllOrders(map,sessionFactory.openSession());
    }
    public List<ActivityOrderVO> getTotalEnrollNumber(Integer sessionTimePeriodId){
        return  activityOrderRepository.findTotalEnrollNumber(sessionTimePeriodId);
    }

    //------------------------------綠界金流方法---------------------------------//
    public String ecpayCheckout(ActivityOrderVO activityOrderVO) {


        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();

        String paymentDescription = activityOrderVO.getSessionVO().getItemVO().getActivityName();

//       String singleProductDetail = new StringBuilder(paymentDescription)
//				.append(activityOrderVO.getEnrollNumber().toString())
//				.append("X")
//				.append(activityOrderVO.getSessionVO().getItemVO().getActivityPrice().toString())
//				.append("=$")
//				.append(activityOrderVO.getEnrollNumber()* activityOrderVO.getSessionVO().getItemVO().getActivityPrice() )
//				.toString();


        //將訂單時間(當前時間)轉為字串放入，以避免編號重複的問題
        Date date = new Date();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderDateString = formatter1.format(date);


        // 訂單號碼(規定大小寫英文+數字)
        obj.setMerchantTradeNo( orderDateString + activityOrderVO.getActivityOrderId());
        // 交易時間(先把毫秒部分切掉)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        obj.setMerchantTradeDate( sdf.format(activityOrderVO.getOrderTime()) );
        // 總金額(總金額 + 總押金)
        obj.setTotalAmount( String.valueOf( activityOrderVO.getPayAmount() ) );
        // 交易描述(沒改)
        obj.setTradeDesc("test Description");
        // 交易商品明細
        obj.setItemName(paymentDescription);
        // 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
        obj.setReturnURL("http://211.23.128.214:5000/");
        obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        //obj.setClientBackURL("http://localhost:8080/backend/rentalorder/rentalCart"); // 問小吳上雲怕爆開(路徑問題)
        String form = all.aioCheckOut(obj, null);

        // 付款完後把付款狀態改為 1 (已付款)
        activityOrderVO.setOrderState(0);

        System.out.println("aaa"+ form);
        return form ;

    }

    //家維新增 == 會員專區用
    public List<ActivityOrderVO> findByMemberId(Integer memberId){
		return activityOrderRepository.findByMemberId(memberId);
	}

}
