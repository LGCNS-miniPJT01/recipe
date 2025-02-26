package recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import recipe.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findById(Long userId);
	User findByuserId(Long userId);
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
    Optional<User> findByUsernameAndPhone(String username, String phone);
    Optional<User> findByUsernameAndEmailAndPhone(String username, String email, String phone);
	Optional<User> findByUsername(String username);
	

}
