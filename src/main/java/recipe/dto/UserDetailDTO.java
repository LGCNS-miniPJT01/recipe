package recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 프로필 조회 수정용 DTO
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  
public class UserDetailDTO {
	private String username;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String description;
}
