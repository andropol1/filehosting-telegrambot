package ru.andropol1.entity;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "binary_content")
@Entity
public class BinaryContent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private byte[] fileAsArrayOfBytes;
}
