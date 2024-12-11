package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.andropol1.dto.AppDocumentStream;
import ru.andropol1.dto.DocumentData;
import ru.andropol1.entity.AppDocument;

import java.util.Optional;

@Repository
public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
	@Query("SELECT d.docName AS docName, d.mimeType AS mimeType, d.binaryContent.fileAsArrayOfBytes AS fileAsArrayOfBytes " +
			"FROM AppDocument d WHERE d.id = :id")
	Optional<DocumentData> findDocumentDataById(@Param("id") Long id);
}
