package ru.andropol1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("ru.andropol1.*")
@EntityScan("ru.andropol1.*")
@ComponentScan("ru.andropol1.*")
@SpringBootApplication
public class RestApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class);
	}
}