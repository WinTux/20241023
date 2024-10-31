package com.wintux._3.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wintux._3.Models.Persona;


@Controller
@RequestMapping("/home") // http://localhost:7001/home
public class PersonalController {
	@PostMapping
	@ResponseBody	// http://localhost:7001/home [POST]
	public String prueba(@RequestBody Persona p) {
		return "OK";
	}
	@GetMapping("/algo/{a}") // http://localhost:7001/home/algo/valor_cualquiera [GET]
	public void prueba2(@PathVariable("a") String valor) {
		System.out.println("Valor unico desde path variable: "+ valor);
	}
}
