package com.wintux._3.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wintux._3.Models.Persona;

@Controller
public class PersonalController {
	@PostMapping
	public String prueba(@RequestBody Persona p) {
		return "OK";
	}
}
