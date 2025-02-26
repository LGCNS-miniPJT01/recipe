package recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import recipe.dto.UserDetailDTO;
import recipe.service.UserService;

@Slf4j 
@RestController
@RequestMapping("/api/users/detail")
@Tag(name ="프로필관리", description = "프로필관리 관련 API 목록")
public class UserDetailController {

	@Autowired
	private UserService userService;
	
	
	// ✅ 프로필 조회 API (GET)
    @GetMapping("/{userId}")
	@Operation(summary = "프로필 조회", description = "프로필 조회 기능 ")
    public ResponseEntity<UserDetailDTO> getProfile(@Parameter(description = "사용자의 ID",required = true) @PathVariable("userId") Long userId) {
        UserDetailDTO userDetailDTO = userService.getProfile(userId);
        return ResponseEntity.ok(userDetailDTO);
    }
    
    // ✅ 프로필 수정 API (PUT) - username, 이미지 url, 자기소개만 가능
    @PutMapping("/{userId}")
    @Operation(summary = "프로필 업데이트 ", description = "프로필 업데이트 기능 ")
    public ResponseEntity<UserDetailDTO> updateProfile(
    		@Parameter(description = "사용자의 ID",required = true) @PathVariable("userId") Long userId,
            @RequestBody UserDetailDTO userDetailDTO) {

        UserDetailDTO updatedUser = userService.updateProfile(userId, userDetailDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
