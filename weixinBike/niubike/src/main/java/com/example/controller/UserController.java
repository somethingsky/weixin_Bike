package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pojo.User;
import com.example.service.UserService;

@Controller
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/genCode")
	@ResponseBody
	public boolean genVerifyCode(String countryCode, String phoneNum) {
		boolean flag=userService.sendMsg(countryCode,phoneNum);
		return flag;
	}
	@RequestMapping("/user/verify")
	@ResponseBody
	public boolean verify(String phoneNum, String verifyCode) {
		//调用Service层，进行校验
		return userService.verify(phoneNum,verifyCode);
	}
	
	
	@RequestMapping("/user/register")
	@ResponseBody
	public boolean register(@RequestBody User user) {//我们可以在参数的前面@RequestBody，接收json类型的参数，然后set到对应的实体类中的属性
		
		boolean flag=true;
		//调用service，将用户的数据保存起来
		try {
			userService.register(user);
		} catch (Exception e) {
			flag=false;
			e.printStackTrace();
			
		}
		return flag;
	}
	
	@RequestMapping("/user/deposite")
	@ResponseBody
	public boolean deposite(@RequestBody User user) {
		boolean flag =true;
		try {
			userService.update(user);
		} catch (Exception e) {
			// 
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}
	
	@RequestMapping("/user/identify")
	@ResponseBody
	public boolean identify(@RequestBody User user) {
		boolean flag=true;
		
		try {
			userService.update(user);
		} catch (Exception e) {
			//
			flag = false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	
}
