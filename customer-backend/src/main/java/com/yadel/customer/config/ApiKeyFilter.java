package com.yadel.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * API key authentication filter.
 * @author y.adel
 */
@Component
@Order(1)
public class ApiKeyFilter implements Filter {
    
    @Value("${api.key}")
    private String validApiKey;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (validApiKey.equals(token)) {
                chain.doFilter(request, response);
                return;
            }
        }
        
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing API key");
    }
}