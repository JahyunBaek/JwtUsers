package com.szs.jobis.Config;

import com.szs.jobis.Config.Provider.JwtSecurityConfig;
import com.szs.jobis.Config.Provider.RefreshTokenProvider;
import com.szs.jobis.Config.Provider.TokenProvider;
import com.szs.jobis.security.handler.JwtAccessDeniedHandler;
import com.szs.jobis.security.handler.JwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/error",
                "/favicon.ico",
                "/swagger-ui.html",
                "/swagger/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // token??? ???????????? ???????????? ????????? csrf??? disable?????????.
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/szs/signup", "/swagger-ui.html").permitAll()
                .and()
                
                
                .csrf()
                .ignoringAntMatchers("/h2-console/**", "/swagger-ui.html")
                .disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler) 


                .and()
                .headers()
                .frameOptions()
                .sameOrigin()


                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // api ??????
                .and()
                .authorizeRequests()
                .antMatchers("/szs/signup").permitAll() // ?????? ??????
                .antMatchers("/szs/login").permitAll() // ?????????
                .antMatchers("/szs/refresh").permitAll() // ?????? Refresh
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider,refreshProvider)).and().build(); // JwtSecurityConfig ??????
    }
}