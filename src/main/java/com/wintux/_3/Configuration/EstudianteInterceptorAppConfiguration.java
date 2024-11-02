package com.wintux._3.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wintux._3.Interceptores.EstudianteInterceptor;

@Component
public class EstudianteInterceptorAppConfiguration implements WebMvcConfigurer{
	@Autowired
	private EstudianteInterceptor interceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
}
