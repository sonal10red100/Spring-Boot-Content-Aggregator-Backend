package com.sonal.contentaggregator;

import com.sonal.contentaggregator.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class ContentAggregatorApplication {

	public static void main(String[] args) {
		//branch test
		//master changed
		SpringApplication.run(ContentAggregatorApplication.class, args);
	}

}
