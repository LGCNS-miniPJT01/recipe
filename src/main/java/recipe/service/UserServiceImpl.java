package recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import recipe.dto.FindEmailRequestDto;
import recipe.dto.FindPasswordRequestDto;
import recipe.dto.LoginRequestDto;
import recipe.dto.ResetPasswordDto;
import recipe.dto.UserDetailDTO;
import recipe.dto.UserRegisterDto;
import recipe.entity.User;
import recipe.entity.UserRole;
import recipe.repository.UserRepository;
import recipe.util.JWTUtil;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired 
	private final UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private JWTUtil jwtUtil;

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

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
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

	// 로그인 기능
	@Override
	public String login(LoginRequestDto loginRequestDto) {
		
		// validation 검증 (NPE)
		if(loginRequestDto.getEmail() == null || loginRequestDto.getEmail().isBlank()) {
			throw new IllegalArgumentException("이메일을 입력해주세요.");
		}
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String roleString = user.getRole().toString();
        return jwtUtil.generateToken(user); // 1시간짜리 JWT 생성
    }

	// 이메일 찾기 
	@Override
	public String findEmail(FindEmailRequestDto findEmailRequestDto) {
		// validation 검증 (NPE)
		if(findEmailRequestDto.getUsername() == null || findEmailRequestDto.getUsername().isBlank()) {
			throw new IllegalArgumentException("사용자 이름을 입력해주세요.");
		}
		
		if(findEmailRequestDto.getPhone() == null || findEmailRequestDto.getPhone().isBlank()) {
			throw new IllegalArgumentException("전화번호를 입력해주세요.");
		}
		
		// 이름과 전화번호가 일치하는 사용자 조회
        User user = userRepository.findByUsernameAndPhone(
                        findEmailRequestDto.getUsername(), 
                        findEmailRequestDto.getPhone())
                .orElseThrow(() -> new IllegalArgumentException("등록된 이메일이 없습니다."));

        // 이메일 마스킹 처리 (ex: te****@gmail.com)
        return maskEmail(user.getEmail());
	}
	
	// 이메일 마스킹 처리 로직
    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex <= 6) return email; // 이메일이 너무 짧으면 그대로 반환

        String prefix = email.substring(0, 2);
        String masked = "****";
        String domain = email.substring(atIndex);

        return prefix + masked + domain;
    }
    

    // 비밀번호 재설정 전 계정 확인 
	@Override
	public boolean findUser(FindPasswordRequestDto requestDto) {
		return userRepository.findByUsernameAndEmailAndPhone(
                requestDto.getUsername(),
                requestDto.getEmail(),
                requestDto.getPhone()
        ).isPresent();
	}

	// 비밀번호 재설정
	@Override
	@Transactional
	public void resetPassword(ResetPasswordDto resetPasswordDto) {
		String email = resetPasswordDto.getEmail();
        String newPassword = resetPasswordDto.getNewPassword();
        String confirmPassword = resetPasswordDto.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
		
	}

	// 프로필 조회 
	@Override
	public UserDetailDTO getProfile(Long userId) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        return new UserDetailDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImageUrl(),
                user.getDescription()
        );
	}

	// 프로필 수정
	@Override
	@Transactional
	public UserDetailDTO updateProfile(Long userId, UserDetailDTO userDetailDTO) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 변경할 값 적용
		if(userDetailDTO.getPhone() != null) {
			user.setPhone(userDetailDTO.getPhone());
		}
        if (userDetailDTO.getProfileImageUrl() != null) {
            user.setProfileImageUrl(userDetailDTO.getProfileImageUrl());
        }
        if (userDetailDTO.getDescription() != null) {
            user.setDescription(userDetailDTO.getDescription());
        }

        return new UserDetailDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImageUrl(),
                user.getDescription()
        );
	}

	@Override
	public Long getUserIdByEmail(String email) {
		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	    return user.getUserId();
	}
}