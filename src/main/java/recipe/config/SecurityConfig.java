package recipe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import recipe.security.JwtRequestFilter;
import recipe.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers( // ✅ 로그인 없이 접근 가능한 경로 설정
                		"/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**" , "/api/**"
                ).permitAll()
                .anyRequest().authenticated()) 
            	.csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화 (WebSocket 사용 시 필수)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // X-Frame-Options 문제 해결
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                ; // JWT 필터 등록

        return http.build();
    }
    
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(customUserDetailsService)
                          .passwordEncoder(passwordEncoder());
        return authManagerBuilder.build();
    }
}
