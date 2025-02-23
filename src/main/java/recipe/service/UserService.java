package recipe.service;

import recipe.dto.FindEmailRequestDto;
import recipe.dto.LoginRequestDto;
import recipe.dto.UserRegisterDto;
import recipe.entity.User;

public interface UserService {
	User getUserById(Long userId);
	User findByEmail(String email); 
	void registerUser(UserRegisterDto userRegisterDto);
	String login(LoginRequestDto loginRequestDto); 
    String findEmail(FindEmailRequestDto findEmailRequestDto);
}
