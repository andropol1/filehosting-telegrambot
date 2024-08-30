package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andropol1.entity.AppDocument;

public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}
