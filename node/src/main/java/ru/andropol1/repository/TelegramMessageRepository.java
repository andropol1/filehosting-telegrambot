package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andropol1.entity.TelegramMessage;

@Repository
public interface TelegramMessageRepository extends JpaRepository<TelegramMessage, Long> {
}
