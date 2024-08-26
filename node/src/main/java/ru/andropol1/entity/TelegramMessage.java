package ru.andropol1.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import javax.persistence.*;
import lombok.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "telegram_message")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class TelegramMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private Update update;
}
