package com.example.Demo.config;


import com.example.Demo.errors.exception.CustomAccessDeniedHandler;
import com.example.Demo.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/file/download/**").permitAll()
                .antMatchers("/", "/csrf", "/v2/api-docs", "/swagger-resources/configuration/ui", "/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .antMatchers("/api/file/**").hasAnyRole("USER")
                .antMatchers("/api/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .httpBasic();
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }


//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/home").setViewName("home");
//        registry.addViewController("/").setViewName("home");
//        registry.addViewController("/hello").setViewName("hello");
//        registry.addViewController("/login").setViewName("login");
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
