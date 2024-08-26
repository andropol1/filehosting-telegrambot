package ru.andropol1.entity;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_document")
public class AppDocument {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String telegramFileId;
	private String docName;
	@OneToOne
	private BinaryContent binaryContent;
	private String mimeType;
	private Long fileSize;

}
