package com.wintux._3.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Primero {
	@Value("${valor.secreto}")
	private String val;
	
	@RequestMapping("/") // http://localhost:8080/ [GET]
	@ResponseBody
	public String saludarAtodos() {
		return "Hola a todos!! :D "+ val;
	}
}
