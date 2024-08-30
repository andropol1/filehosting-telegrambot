package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andropol1.entity.AppUser;
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	AppUser findAppUserByTelegramUserId(Long id);
}
