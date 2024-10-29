package com.wintux._3.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Primero {
	@Value("${valor.secreto}")
	private String val;
	@Autowired
	private ApplicationContext context;
	
	private static final Logger logger = LoggerFactory.getLogger(Primero.class);
	
	@RequestMapping("/") // http://localhost:8080/ [GET]
	@ResponseBody
	public String saludarAtodos() {
		logger.debug("Se acaba de llamar al endpoint /.");
		return "Hola a todos!! :D "+ val;
	}
	@RequestMapping("/cerrar") // http://localhost:8080/cerrar [GET]
	@ResponseBody
	public void cerrar() {
		logger.info("Se acaba de llamar al endpoint /cerrar.");
		SpringApplication.exit(context);
	}
	
}
