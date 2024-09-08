package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andropol1.entity.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByTelegramUserId(Long id);

	Optional<AppUser> findByEmail(String email);
}
