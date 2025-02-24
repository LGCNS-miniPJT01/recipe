package recipe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/v3/api-docs/**",
                    "/swagger-resources/**", 
                    "/webjars/**",
                    // 나중에 비로그인 사용자 기능을 추가하려면 아래 url 추가 
                    "/api/users/register",  // 회원가입
                    "/api/users/login", // 로그인
                    "/api/users/**," +
                    "/api/recipe/**",
                    "api/comments/**"
               ).permitAll()  // Swagger 경로는 인증 없이 접근 가능
                .anyRequest().authenticated()  // 그 외의 요청은 인증 필요
            )
            .csrf(csrf -> csrf.disable());  // 🚨 CSRF 보호 비활성화 (테스트 환경에서만)

        return http.build();
    }

}