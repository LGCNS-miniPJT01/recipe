package recipe.Initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import recipe.entity.User;
import recipe.entity.UserRole;
import recipe.repository.UserRepository;

// 개발환경 admin 계정 insert
// 도커 올릴때 init.sql 내용 추가하고 해당 클래스 삭제 예정
@Slf4j
@Component
@Order(1)
public class AdminUserInitial implements CommandLineRunner{
	 	@Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Override
	    public void run(String... args) throws Exception {
	    	if (!userRepository.existsByEmail("admin123@test.com")) { // ID 대신 email로 확인
	    	    User admin = new User();
	    	    admin.setEmail("admin123@test.com");
	    	    admin.setPasswordHash(passwordEncoder.encode("admin1234!"));
	    	    admin.setPhone("010-1234-1234");
	    	    admin.setRole(UserRole.ADMIN);
	    	    admin.setUsername("관리자");

	    	    userRepository.save(admin);
	    	    log.info("✅ 관리자 계정이 추가되었습니다.");
	    	} else {
	    	    log.info("✅ 관리자 계정이 이미 존재합니다.");
	    	}
	    }
}
