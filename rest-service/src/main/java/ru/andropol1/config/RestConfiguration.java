package ru.andropol1.config;

import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfiguration {
	@Bean
	public DecoderProperties decoderProperties(){
		return new DecoderProperties();
	}
	@Bean
	public Hashids getHashids(){
		return new Hashids(decoderProperties().getSalt(), decoderProperties().getMinHashLength());
	}
}
