package recipe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 회원가입 DTO
@Getter
@Setter
public class UserRegisterDto {
	
	@NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "전화번호를 입력하세요.")
    @Size(min = 10, max = 15, message = "전화번호는 10~15자리여야 합니다.")
    private String phone;

    @NotBlank(message = "사용자명을 입력하세요.")
    @Size(min = 2, max = 50, message = "사용자명은 2~50자 사이여야 합니다.")
    private String username;

}
