package com.wintux._3.Filtros;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
@Component
@Order(1)
public class ControlConexionFilter  implements Filter{
	@Autowired
	ObjectMapper objectMapper;
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Host remoto: "+ request.getRemoteHost());
		System.out.println("Address remoto: "+ request.getRemoteAddr());
		chain.doFilter(request, response);
	}
	
}
