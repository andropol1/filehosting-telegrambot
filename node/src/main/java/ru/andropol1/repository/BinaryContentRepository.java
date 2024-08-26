package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andropol1.entity.BinaryContent;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}
