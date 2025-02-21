package recipe.service;

import recipe.dto.UserRegisterDto;
import recipe.entity.User;

public interface UserService {
	User getUserById(Long userId);
	User findByEmail(String email); 
	void registerUser(UserRegisterDto userRegisterDto);
}
