package com.member.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.member.model.RecaptchaResponse;
import com.notification.model.NotificationService;
import com.notification.model.NotificationVO;

import redis.clients.jedis.Jedis;

@Controller
@Validated
@RequestMapping("/member")
public class MemberLoginController {

	@Autowired
	MemberService memSvc;
	
	@Autowired
	NotificationService notiSvc;

	@Autowired
	private JavaMailSender mailSender;	
	
	@Value("${google.recaptcha.secret-key}")
    private String recaptchaSecretKey;

	@GetMapping("/memberLogin")
	public String memberLogin(Model model) {
		MemberVO memberVO = new MemberVO();
		model.addAttribute("memberVO", memberVO);

		return "/front_end/member/memberLogin";
	}

	//練習方法級別驗證
	@PostMapping("/memberLogin")
	public String memberLogin(HttpServletRequest req, HttpServletResponse res,
			
			@RequestParam("g-recaptcha-response") String captchaResponse,

			@NotEmpty(message = "電子信箱:請勿空白") @RequestParam("email") String email,

			@NotEmpty(message = "會員密碼:請勿空白") @RequestParam("password") String password, 
			
			ModelMap model) {
		
		
		String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + recaptchaSecretKey + "&response=" + captchaResponse;
        RecaptchaResponse recaptchaResponse = new RestTemplate().postForObject(url, null, RecaptchaResponse.class);
    	
        	//機器人驗證有過
        if (recaptchaResponse.isSuccess()) {
        	
        	MemberVO memberVO = memSvc.findByEmail(email);
    		
    		//查無此使用者帳號(信箱)
    		if (memberVO == null) {
    			model.addAttribute("status", "wrong");

    			return "/front_end/member/memberLogin";
    		}
    		
    		//帳號密碼輸入錯誤
    		if (!memberVO.getEmail().equals(email) || !memberVO.getPassword().equals(password)) {
    			model.addAttribute("status", "failed");

    			return "/front_end/member/memberLogin";
    		}
    		
    		
        
    		//驗證通過,將資訊存至session給過濾器做驗證
    		HttpSession session = req.getSession();
    		model.addAttribute("memberVO", memberVO);
    		session.setAttribute("memberVO", memberVO);
    		session.setAttribute("account", email);
    		
    		
    		NotificationVO notiVO = new NotificationVO();
			notiVO.setMember(memberVO);
			notiVO.setTitle("登入通知!");
			notiVO.setType(3);
			notiVO.setStatus(0);
    		
    		Date date = new Date();
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = formatter1.format(date);
			
			
			//寄送會員登入通知
			notiVO.setContent("親愛的貴賓"+memberVO.getMemberName()+"您好，您已於"+nowTime+"時登入專區!若您沒有印象有進行登入，請盡速變更您的密碼!並與客服聯繫!");
			notiSvc.sendMsg(notiVO);
    		
			//供WebSocket隨時調用
			Integer count = notiSvc.getNotiUnread(memberVO.getMemberId());		
			model.addAttribute("UnreadCount",count);
			
    		//檢查有無來源地址,若沒有就到會員專區頁面
    		try {
    			String location = (String) session.getAttribute("location");
    			if (location != null) {
    				session.removeAttribute("location"); // 看看有無來源網頁 (-->如有來源網頁:則重導至來源網頁)
    				return "redirect:" + location;
//    				res.sendRedirect(location);
//    				return;
    			}
    		} catch (Exception ignored) {
    			ignored.printStackTrace();
    		}

    		return "/front_end/member/memberOnlyWeb";
        	
        	
        	//機器人驗證沒過
        } else {
			model.addAttribute("status", "robot");
            return "/front_end/member/memberLogin";
        }
		
	}

	
	
	
	


	
	
	
	
	
	
	
	@GetMapping("/forgetPassword")
	public String forgetPassword(Model model) {
		return "front_end/member/forgetPassword";
	}

	@PostMapping("/forgetMail")
	public String forgetMail(
			HttpServletRequest req,
			@NotEmpty(message = "電子信箱:請勿空白") 
			@RequestParam("email") String email, 
			Model model)
			throws MessagingException, UnsupportedEncodingException {

		// 錯誤處理
		MemberVO memberVO = memSvc.findByEmail(email);
		if (memberVO == null) {
			model.addAttribute("status", "failed");
			return "front_end/member/forgetPassword";
		}
		

		// 驗證碼產生--小吳Redis範例

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 6; i++) {
			int condition = (int) (Math.random() * 3) + 1;
			switch (condition) {
			case 1:
				char c1 = (char) ((int) (Math.random() * 26) + 65);
				sb.append(c1);
				break;
			case 2:
				char c2 = (char) ((int) (Math.random() * 26) + 97);
				sb.append(c2);
				break;
			case 3:
				sb.append((int) (Math.random() * 10));
			}
		}

		String mailToken = sb.toString();
		
		Jedis jedis = new Jedis("localhost", 6379);
		jedis.set("changePasswordToken:"+memberVO.getMemberId(),mailToken);
		jedis.expire("changePasswordToken:"+memberVO.getMemberId(),180);

		
		// 信件寄送
		
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);

		helper.setTo(email);
		helper.setFrom("david70306@yahoo.com.tw", "CactusHotel");
		helper.setSubject("忘記密碼驗證信");

		String text = "<p>親愛的" + memberVO.getMemberName() + "貴賓您好!</p>" + "<p>此為您的驗證碼「<span style='color:red'>"
				+ mailToken + "</span>」</p>" + "<p>請將驗證碼輸入後重新修改您的密碼!</p>" 
				+ "<p>該驗證碼的時效性為<span style='color:red'>3分鐘</span>，請在時效內進行驗證!</p>";

		helper.setText(text, true);

		mailSender.send(msg);
		
		
		HttpSession session = req.getSession();
		session.setAttribute("memberVOchangePassword", memberVO);
		model.addAttribute("status", "success");
		return "front_end/member/forgetTokenConfirm";
	}

	@PostMapping("/forgetTokenConfirm")
	public String forgetTokenConfirm(@RequestParam("token") String inputToken, ModelMap model,HttpSession session) {
		
		MemberVO memberVO = (MemberVO)session.getAttribute("memberVOchangePassword");
		
		Jedis jedis = new Jedis("localhost", 6379);
		String mailToken = jedis.get("changePasswordToken:"+memberVO.getMemberId());
		
		//錯誤處理
		if (mailToken != null &&!inputToken.equals(mailToken)) {
			model.addAttribute("status", "failed");
			return "front_end/member/forgetTokenConfirm";
		} else if(mailToken == null){
			model.addAttribute("status", "timeout");
			return "front_end/member/forgetTokenConfirm";
		}else {
			model.addAttribute("status", "success");
			return "front_end/member/changePassword";
		}

	}
	
	@PostMapping("/changePassword")
	public String changePassword (
			HttpServletRequest req,
			@RequestParam("password") String password,
			@RequestParam("rePassword") String rePassword,
			ModelMap model
			) {
		
		if(!password.equals(rePassword)) {
			model.addAttribute("status", "failed");
			return "front_end/member/changePassword";
		}
		
			//密碼更改完成
			HttpSession session = req.getSession();
			MemberVO memberVO = (MemberVO)session.getAttribute("memberVOchangePassword");
			memSvc.updatePassword(password, memberVO.getMemberId());
			
			//寄送通知給使用者
			NotificationVO notiVO = new NotificationVO();
			notiVO.setMember(memberVO);
			notiVO.setTitle("您的密碼已被修改!");
			notiVO.setType(3);
			notiVO.setStatus(0);
			
			Date date = new Date();
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = formatter1.format(date);
			
			//寄送更改密碼通知
			notiVO.setContent("您的密碼已於"+nowTime+"時被修改!若您沒有印象有進行密碼修改請求，請盡速變更您的密碼!並與客服聯繫!");
			notiSvc.sendMsg(notiVO);
			
			session.removeAttribute("memberVOchangePassword");
			model.addAttribute("memberVO", memberVO);
			model.addAttribute("status", "changeFinish");
			return "front_end/member/memberLogin";
		
	}
	

	
	
	
	
	
	
	
	
	@GetMapping("/memberLogout")
	public String memberLogout(HttpSession session, ModelMap model) {
		if (session != null) {
			//移除session上之屬性,使對應網頁重新進入過濾器控制範圍
			session.removeAttribute("account");
			session.removeAttribute("memberVO");
		}

		MemberVO memberVO = new MemberVO();
		model.addAttribute("memberVO", memberVO);

		return "front_end/member/memberLogin";
	}
	
	
	
	
	//方法級別驗證使用--捕捉ConstraintViolationException做處理
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ModelAndView handleError(
			HttpServletRequest req,
			ConstraintViolationException e,
			Model model) {
		
		//將錯誤訊息收集
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    
	    StringBuilder strBuilder = new StringBuilder();
	    
	    for (ConstraintViolation<?> errorSet : violations ) {
	    	//把錯誤訊息串接 
	    	strBuilder.append(errorSet.getMessage() + "<br>");
	    }
	    
		String message = strBuilder.toString();
	    return new ModelAndView("front_end/member/memberLogin", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
}
