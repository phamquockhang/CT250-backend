package com.dvk.ct250backend.app.util;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatApiResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpServletResponse.getStatus();
        ApiResponse<Object> apiResponse = new ApiResponse<Object>();
        apiResponse.setStatus(status);
        if(status >= 400){
            apiResponse.setError("Call API Error");
            apiResponse.setMessage(body);
        }else{
            apiResponse.setData(body);
            apiResponse.setMessage("Call API Success");
        }

        return apiResponse;
    }
}

