package recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import recipe.dto.LoginRequestDto;
import recipe.dto.UserRegisterDto;
import recipe.service.UserServiceImpl;

@Slf4j 
@RestController
@RequestMapping("/api/users")
@Tag(name ="회원관리 ", description = "회원관리 관련 API 목록")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@PostMapping("/register")
	@Operation(summary = "회원가입", description = "회원가입기능")
    public ResponseEntity<String> registerUser(@RequestBody @Validated UserRegisterDto userRegisterDto) {
		log.info("📍[회원가입 요청] email: {}, username: {}", userRegisterDto.getEmail(), userRegisterDto.getUsername());
        userService.registerUser(userRegisterDto);
        log.info("✅ [회원가입 완료] email: {}", userRegisterDto.getEmail());
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
	
	// 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 기능")
    public ResponseEntity<String> login(@RequestBody @Validated LoginRequestDto loginRequestDto) {
		log.info("📍[로그인 요청] email: {}, PW: {}", loginRequestDto.getEmail(), loginRequestDto.getPassword());
        String token = userService.login(loginRequestDto);
        log.info("✅ [로그인  완료] email: {}", loginRequestDto.getEmail());
        return ResponseEntity.ok(token); // JWT 토큰 반환
    }

}
