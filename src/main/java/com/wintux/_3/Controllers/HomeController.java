package com.wintux._3.Controllers;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/home") // http://localhost:7001/home
public class HomeController {
	
	private static final Logger logger = Logger.getLogger(HomeController.class.getName());
	
	//@GetMapping // http://localhost:7001/home [GET]
	//@ResponseBody
	@RequestMapping(method=RequestMethod.GET)
	public String index() {
		logger.warning("Se alcanza al endpoint /home ¿se retorna la página?");
		//sereve, warning, info, config, fine, finer, finest
		return "home/index";
	}
}
