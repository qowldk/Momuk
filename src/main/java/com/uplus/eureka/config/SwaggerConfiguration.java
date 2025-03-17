package com.uplus.eureka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public SwaggerConfiguration() {
		logger.debug("SwaggerConfiguration.................");
	}
	@Bean
	public OpenAPI openEurekaAPI() {
		logger.debug("openEurekaAPI.............");
		Info info = new Info().title("MoMuk API 명세서").description(
				"<h3>SpringTest API Reference for Developers</h3>Swagger를 이용한 MoMuk's API<br><img src=\"/eureka/assets/img/eureka_logo.png\" width=\"50\">")
				.version("v1")
				.contact(new io.swagger.v3.oas.models.info.Contact().name("MoMuk Team")
				.email("team's email").url("http://eureka.uplus.com"));

		return new OpenAPI().components(new Components()).info(info);
	}
	
	
	@Bean
	public GroupedOpenApi userApi() {
		return GroupedOpenApi.builder().group("momuk-user").pathsToMatch("/member/**").build();
	}

	@Bean
	public GroupedOpenApi voteApi() {
		return GroupedOpenApi.builder().group("momuk-vote").pathsToMatch("/book/**").build();
	}
	@Bean
	public GroupedOpenApi participantApi() {
		return GroupedOpenApi.builder().group("momuk-participant").pathsToMatch("/test/**").build();
	}
	 
}