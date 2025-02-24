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
import recipe.dto.FindEmailRequestDto;
import recipe.dto.FindPasswordRequestDto;
import recipe.dto.LoginRequestDto;
import recipe.dto.ResetPasswordDto;
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
    
    // 이메일 찾기
    @PostMapping("/findemail")
    @Operation(summary ="이메일 찾기", description = "이름,전화번호로 이메일 찾기 기능")
    public ResponseEntity<String> findEmail(@RequestBody FindEmailRequestDto findEmailRequestDto) {
		log.info("📍[이메일 찾기 요청] username: {}, phone: {}", findEmailRequestDto.getUsername(), findEmailRequestDto.getPhone());
        String maskedEmail = userService.findEmail(findEmailRequestDto);
        log.info("✅ [이메일 찾기 완료]");
        return ResponseEntity.ok(maskedEmail);
    }
    
    // 1️⃣ 비밀번호 재설정을 위한 계정찾기 
    @PostMapping("/findpwuser")
    @Operation(summary ="비밀번호 재설정 사용자 계정 확인", description = "이메일,이름,전화번호로 계정 찾기 기능")
    public ResponseEntity<String> findUser(@RequestBody FindPasswordRequestDto requestDto) {
		log.info("📍[계정 찾기 요청 ]");
        boolean userExists = userService.findUser(requestDto);
        if (userExists) {
            return ResponseEntity.ok("사용자 확인됨");
        } else {
            return ResponseEntity.badRequest().body("등록된 계정을 찾을 수 없습니다.");
        }
    }
    
    // 2️⃣ 비밀번호 변경 API (DTO 사용)
    @PostMapping("/resetpassword")
    @Operation(summary = "비밀번호 변경 요청", description = "사용자 입력값으로 비밀번호 변경")
    public ResponseEntity<String> resetPassword(@RequestBody @Validated ResetPasswordDto resetPasswordDto) {
    	log.info("📍[비밀번호 변경 요청]");
        userService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 유저 목록 조회
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestBody Long userId) {
    	User adminCheck = userService.getUserById(userId);
    	if (adminCheck == null || !adminCheck.isAdmin()) {
            return ResponseEntity.status(403).body(null); // 권한 없으면 403 반환
        }
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 유저 정지 (Suspended 상태 변경)
    @PutMapping("/users/{userId}/suspend")
    public ResponseEntity<User> suspendUser(@PathVariable Long stopId, @RequestBody Long userId) {
    	User adminCheck = userService.getUserById(userId);
    	if (adminCheck == null || !adminCheck.isAdmin()) {
            return ResponseEntity.status(403).body(null); // 권한 없으면 403 반환
        }
        User suspendedUser = adminService.suspendUser(stopId);
        return ResponseEntity.ok(suspendedUser);
    }

    // 유저 활성화 (Suspended 상태 변경)
    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long activateId, @RequestBody Long userId) {
    	User adminCheck = userService.getUserById(userId);
    	if (adminCheck == null || !adminCheck.isAdmin()) {
            return ResponseEntity.status(403).body(null); // 권한 없으면 403 반환
        }
        User activatedUser = adminService.activateUser(activateId);
        return ResponseEntity.ok(activatedUser);
    }
}
