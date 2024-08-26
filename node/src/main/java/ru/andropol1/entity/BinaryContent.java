package ru.andropol1.entity;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "binary_content")
public class BinaryContent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private byte[] fileAsArrayOfBytes;
}
