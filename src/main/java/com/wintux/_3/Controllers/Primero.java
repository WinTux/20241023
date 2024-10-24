package com.wintux._3.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Primero {
	@RequestMapping("/") // http://localhost:8080/ [GET]
	@ResponseBody
	public String saludarAtodos() {
		return "Hola a todos!! :D";
	}
}
