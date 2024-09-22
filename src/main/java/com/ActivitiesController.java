package com;

import java.util.List;

import com.activities_attendees.model.AttendeesService;
import com.activities_attendees.model.AttendeesVO;
import com.activities_order.model.ActivityOrderService;
import com.activities_order.model.ActivityOrderVO;
import com.activities_promotion.model.PromotionService;
import com.activities_promotion.model.PromotionVO;
import com.activities_session.model.SessionVO;
import com.activities_session.model.SessionService;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.session_time_period.model.Time_PeriodVO;
import com.session_time_period.model.Time_PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.activities_category.model.CategoryVO;
import com.activities_category.model.CategoryService;
import com.activities_item.model.ItemVO;
import com.activities_item.model.ItemService;

@Controller
public class ActivitiesController {
	//1
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ItemService itemService;

	@Autowired
	SessionService sessionService;

	@Autowired
	Time_PeriodService time_periodService;

	@Autowired
	PromotionService promotionService;

	@Autowired
	ActivityOrderService activityOrderService;

	@Autowired
	MemberService memberService;

	@Autowired
	AttendeesService attendeesService;
	

	
	@GetMapping("/category/select_category")
	public String selectCategory(Model model) {
		return "back_end/category/select_category";
	}
	
	@GetMapping("/category/listAllCategory")
	public String listAllCategory(Model model) {
		return "back_end/category/listAllCategory";
	}
	
	@ModelAttribute("categoryListData") // 下拉選單、SHOW跑出DB已有的值 for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<CategoryVO> referenceListData(Model model){
		
		List<CategoryVO> list = categoryService.getAll();
		return list;
	}
	
	
	@GetMapping("/item/select_item")
	public String selectItem(Model model) {
		return "back_end/item/select_item";
	}

	//到前台
	@GetMapping("/activity/select_item")
	public String selectItemFront(Model model) {
		return "front_end/activity/select_item";
	}
	
	@GetMapping("/item/listAllItem")
	public String listAllItem(Model model) {
		return "back_end/item/listAllItem";
	}
	
	@ModelAttribute("itemListData") // 下拉選單、SHOW跑出DB已有的值 for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ItemVO> referenceListData2(Model model){
		
		List<ItemVO> list = itemService.getAll();
		return list;
	}
	
	@ModelAttribute("categoryListData2") // 下拉選單、SHOW跑出DB已有的值 for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<CategoryVO> referenceListData_Cate(Model model){
		model.addAttribute("categoryVO", new CategoryVO());
		List<CategoryVO> list = categoryService.getAll();
		return list;
	}

	@GetMapping("/session/select_session")
	public String selectSession(Model model) {
		return "back_end/session/select_session";
	}

	@GetMapping("/session/listAllSession")
	public String listAllSession(Model model) {

		return "back_end/session/listAllSession";
	}

	@ModelAttribute("sessionListData") // 下拉選單、SHOW跑出DB已有的值 for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<SessionVO> referenceListData_Sess(Model model){
		model.addAttribute("sessionVO", new SessionVO());
		List<SessionVO> list = sessionService.getAll();

		return list;
	}

	@ModelAttribute("itemListData2") // 下拉選單、SHOW跑出DB已有的值 for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<ItemVO> referenceListData_Item(Model model){
		model.addAttribute("itemVO", new ItemVO());
		List<ItemVO> list = itemService.getAll();
		return list;
	}

	@ModelAttribute("timePeriodListData") // 下拉選單、SHOW跑出DB已有的值 for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<Time_PeriodVO> referenceListData_Time(Model model){
		model.addAttribute("time_PeriodVO", new Time_PeriodVO());
		List<Time_PeriodVO> list = time_periodService.getAll();
		return list;
	}

	@GetMapping("/promotion/select_promotion")
	public String selectPromotion(Model model) {
		return "back_end/promotion/select_promotion";
	}

	@GetMapping("/promotion/listAllPromotion")
	public String listAllPromotion(Model model) {
		return "back_end/promotion/listAllPromotion";
	}

	@ModelAttribute("promotionListData") // 下拉選單、SHOW跑出DB已有的值 for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<PromotionVO> referenceListPro(Model model){
		model.addAttribute("promotionVO", new PromotionVO());
		List<PromotionVO> list = promotionService.getAll();
		return list;
	}

	@GetMapping("/activityOrder/select_order")
	public String selectActivityOrder(Model model) {
		return "back_end/activityOrder/select_order";
	}

	@GetMapping("/activityOrder/listAllOrder")
	public String listAllActivityOrder(Model model) {
		return "back_end/activityOrder/listAllOrder";
	}



	@ModelAttribute("activityOrderListData")
	protected List<ActivityOrderVO> referenceListDataAc(Model model) {
		model.addAttribute("activityOrderVO", new ActivityOrderVO());
		List<ActivityOrderVO> list = activityOrderService.getAll();

		// Ensure promotionVO is initialized
		// 如果為null創建新的新的PromotionVO，並賦值給它
		for (ActivityOrderVO order : list) {
			if (order.getPromotionVO() == null) {
				order.setPromotionVO(new PromotionVO());
			}
		}

		return list;
	}

	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListDataMe(Model model) {
		model.addAttribute("memberVO", new MemberVO());
		List<MemberVO> list = memberService.getAll();
		return list;
	}

	@ModelAttribute("sessionListData")
	protected List<SessionVO> referenceListDataSe(Model model) {
		model.addAttribute("sessionVO", new SessionVO());
		List<SessionVO> list = sessionService.getAll();
		return list;
	}

	@ModelAttribute("promotionListData")
	protected List<PromotionVO> referenceListDataPr(Model model) {
		model.addAttribute("promotionVO", new PromotionVO());
		List<PromotionVO> list = promotionService.getAll();
		return list;
	}

	@GetMapping("/attendees/select_attendees")
	public String selectAttendees(Model model) {
		return "back_end/attendees/select_attendees";
	}

	@GetMapping("/attendees/listAllAttendees")
	public String listAllAttendees(Model model) {
		return "back_end/attendees/listAllAttendees";
	}

	@ModelAttribute("attendeesListData")
	protected List<AttendeesVO> referenceListDataAt(Model model) {
		model.addAttribute("attendeesVO", new AttendeesVO());
		List<AttendeesVO> list = attendeesService.getAll();
		return list;
	}

	@GetMapping("/timePeriod/listAllTimePeriod")
	public String listAllTimePeriod(Model model) {
		return "back_end/timePeriod/listAllTimePeriod";
	}


	@GetMapping("/session/selectTime")
	public String selectTime(Model model) {

		return "back_end/session/selectTime";
	}

	@GetMapping("/session/selectTime2")
	public String selectTime2(Model model) {

		return "back_end/session/selectTime2";
	}

}


