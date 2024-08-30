package ru.andropol1.config;

import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfiguration {
	@Bean
	public HashProperties hashProperties(){
		return new HashProperties();
	}
	@Bean
	public Hashids getHashids(){
		return new Hashids(hashProperties().getSalt(), hashProperties().getMinHashLength());
	}
}
