package recipe.service;

import recipe.dto.FindEmailRequestDto;
import recipe.dto.FindPasswordRequestDto;
import recipe.dto.LoginRequestDto;
import recipe.dto.ResetPasswordDto;
import recipe.dto.UserDetailDTO;
import recipe.dto.UserRegisterDto;
import recipe.entity.User;

public interface UserService {
	User getUserById(Long userId);
	User getUserByUsername(String username);
	User findByEmail(String email); 
	void registerUser(UserRegisterDto userRegisterDto);
	String login(LoginRequestDto loginRequestDto); 
    String findEmail(FindEmailRequestDto findEmailRequestDto);
    boolean findUser(FindPasswordRequestDto requestDto);
    void resetPassword(ResetPasswordDto resetPasswordDto);
    UserDetailDTO getProfile(Long userId);
    UserDetailDTO updateProfile(Long userId, UserDetailDTO userDetailDTO);
    Long getUserIdByEmail(String email);
}
