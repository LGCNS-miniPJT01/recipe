package recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import recipe.dto.UserRegisterDto;
import recipe.entity.User;
import recipe.entity.UserRole;
import recipe.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired 
	private final UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //ID로 유저 조회 (없으면 예외 발생)
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 없습니다."));
	}

	// 회원가입
	@Override
	@Transactional
	public void registerUser(UserRegisterDto userRegisterDto) {
		// TODO Auto-generated method stub
		 // 이메일 중복 확인
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // User 엔티티 생성
        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRegisterDto.getPassword())); // 비밀번호 해싱
        user.setPhone(userRegisterDto.getPhone());
        user.setUsername(userRegisterDto.getUsername());
        user.setRole(UserRole.USER); // 기본 역할은 USER

        // 데이터베이스 저장
        userRepository.save(user);
		
	}
}