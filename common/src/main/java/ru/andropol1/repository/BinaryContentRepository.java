package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andropol1.entity.BinaryContent;
@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}
