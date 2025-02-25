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
            .authorizeHttpRequests(auth -> auth
                .requestMatchers( // ✅ 로그인 없이 접근 가능한 경로 설정
                    "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", // Swagger 허용
                    "/api/users/register", "/api/users/login", // 회원가입 및 로그인 허용
                    "/api/recipes/**", "/api/comments/**", "/api/recipesearch/**", // 검색, 레시피 접근 허용
                    "/ws/**" , "/topic/**", "/queue/**"// WebSocket & 정적 파일 허용
                ).permitAll()
                .anyRequest().authenticated()) 
            	.csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화 (WebSocket 사용 시 필수)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // X-Frame-Options 문제 해결


        return http.build();
    }
}
