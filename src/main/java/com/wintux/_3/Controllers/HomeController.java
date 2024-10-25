package com.wintux._3.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/home") // http://localhost:7001/home
public class HomeController {
	//@GetMapping // http://localhost:7001/home [GET]
	//@ResponseBody
	@RequestMapping(method=RequestMethod.GET)
	public String index() {
		return "home/index";
	}
}
