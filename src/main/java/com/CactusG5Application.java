package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ServletComponentScan
@SpringBootApplication
//@ComponentScan(basePackages = {"com.*"},basePackageClasses = {ActivitiesController.class, IndexController_inSpringBoot.class})
public class CactusG5Application {

	public static void main(String[] args) {
		SpringApplication.run(CactusG5Application.class, args);
	}

}
