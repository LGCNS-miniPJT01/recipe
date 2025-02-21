package recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import recipe.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findById(Long userId);
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}