package recipe.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import recipe.entity.User;
import recipe.repository.UserRepository;
import recipe.service.CustomUserDetailsService;
import recipe.util.JWTUtil;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtils;
    
    @Autowired
    private UserRepository repository;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		log.info("JwtRequestFilter >>>>");
	        
        String jwtToken = null;
        String subject = null;
        
        // Authorization 요청 헤더 포함 여부를 확인하고, 헤더 정보를 추출
        
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        log.info(">>>>>>>>>>>>> ",authorizationHeader);
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            subject = jwtUtils.getSubjectFromToken(jwtToken);
        } else {
            log.error("Authorization 헤더 누락 또는 토큰 형식 오류");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            response.getWriter().flush();
            return;
        }
        
        
        Optional<User> user = repository.findByUsername(subject);

		User userEntity = user.orElseThrow(() -> new UsernameNotFoundException("등록된 사용자가 없습니다."));
		
        if (!jwtUtils.validateToken(jwtToken, userEntity)) {
            log.error("사용자 정보가 일치하지 않습니다. ");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            response.getWriter().flush();
            return;
        }
        
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject);
        
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken 
            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        
        log.info(">>>>>>>>>>>", SecurityContextHolder.getContext().getAuthentication());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                
        
        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        log.debug("shouldNotFilter >>>>>>>>>>");
//
//        
//        String[] excludePath = { 
//                "/swagger-ui", "/swagger-ui/", "/swagger-ui/**",
//                "/v3/api-docs", "/v3/api-docs/**",
//                "/swagger-resources", "/swagger-resources/**",
//                "/webjars", "/webjars/**",
//                "/api/**",
//                "/ws/**" , "/topic/**", "/queue/**"
//            };
//       
//        
//        String uri = request.getRequestURI();
//        boolean result = Arrays.stream(excludePath).anyMatch(uri::startsWith);
//        log.debug(">>>" + result);
//        
        return true;
    }
}
