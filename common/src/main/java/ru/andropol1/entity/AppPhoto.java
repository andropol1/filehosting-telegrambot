package ru.andropol1.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_photo")
@Entity
public class AppPhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String telegramFileId;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "binary_content_id", referencedColumnName = "id")
	private BinaryContent binaryContent;
	private Integer fileSize;
}
