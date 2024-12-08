package ru.andropol1.entity;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_document")
@Entity
public class AppDocument {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String telegramFileId;
	private String docName;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "binary_content_id", referencedColumnName = "id")
	private BinaryContent binaryContent;
	private String mimeType;
	private Long fileSize;

}
