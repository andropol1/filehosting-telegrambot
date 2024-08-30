package ru.andropol1.entity;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.andropol1.enums.UserState;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long telegramUserId;
	@CreationTimestamp
	private LocalDateTime firstLoginDate;
	private String firstName;
	private String lastName;
	private String userName;
	private String email;
	private Boolean isActive;
	@Enumerated(EnumType.STRING)
	private UserState userState;
}
