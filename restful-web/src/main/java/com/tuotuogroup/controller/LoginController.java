package com.tuotuogroup.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	/*
	 * 日志
	 */
	protected Log log = LogFactory.getLog(getClass());;
	
	/**
	 * @描述：跳转到工程主体框架页面
	 *
	 */
	@RequestMapping(value = "toIndex")
	public String toIndex(HttpServletRequest request, Model model, HttpServletResponse response, String projectName) {
		//step1 获取顶部与左侧树菜单
		//TODO 这里跳转到布局index.jsp页面，从数据库中去获取顶部与左侧菜单
		return "layout/index";

	}
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Map<String, Object> loginUser(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", "true");
			map.put("msg", "登录成功！");
		return map;
	}



}
