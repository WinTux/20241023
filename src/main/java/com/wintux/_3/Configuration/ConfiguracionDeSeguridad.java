package com.wintux._3.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.wintux._3.Filtros.ControlConexionFilter;

import jakarta.servlet.Filter;

@Configuration
public class ConfiguracionDeSeguridad {
	@Autowired
	private ControlConexionFilter filtro;
	
	FilterRegistrationBean<Filter> miFilterRegBean(){
		FilterRegistrationBean<Filter> fil = new FilterRegistrationBean<Filter>();
		fil.setFilter(filtro);
		fil.addUrlPatterns("/estudiante/*");
		fil.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return fil;
	}
}
