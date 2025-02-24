package recipe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import recipe.dto.LoginRequestDto;
import recipe.dto.UserRegisterDto;
import recipe.entity.User;
import recipe.service.AdminService;
import recipe.service.UserServiceImpl;

@Slf4j 
@RestController
@RequestMapping("/api/users")
@Tag(name ="회원관리 ", description = "회원관리 관련 API 목록")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private AdminService adminService;
	
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

    // 유저 목록 조회
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 유저 정지 (Suspended 상태 변경)
    @PutMapping("/users/{userId}/suspend")
    public ResponseEntity<User> suspendUser(@PathVariable Long userId) {
        User suspendedUser = adminService.suspendUser(userId);
        return ResponseEntity.ok(suspendedUser);
    }

    // 유저 활성화 (Suspended 상태 변경)
    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long userId) {
        User activatedUser = adminService.activateUser(userId);
        return ResponseEntity.ok(activatedUser);
    }
}
