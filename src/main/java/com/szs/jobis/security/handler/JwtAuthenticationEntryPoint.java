package com.szs.jobis.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.jobis.Dto.CommonResponse;
import com.szs.jobis.Dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 인증 실패 시
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private ObjectMapper objectMapper;
    @PostConstruct
    void init() {
        objectMapper = new ObjectMapper();
    }
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401(인증 실패)
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("UNAUTHORIZED")
                .code("AUTH")
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(objectMapper.writeValueAsString(CommonResponse.builder()
                        .success(false)
                        .error(error)
                .build()));
    }
}