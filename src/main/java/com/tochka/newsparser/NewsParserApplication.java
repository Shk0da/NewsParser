package com.tochka.newsparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class NewsParserApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(NewsParserApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NewsParserApplication.class);
	}
}
