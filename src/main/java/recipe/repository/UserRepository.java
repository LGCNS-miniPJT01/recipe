package recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipe.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findById(Long userId);
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
	boolean existsByEmail(String email);
}