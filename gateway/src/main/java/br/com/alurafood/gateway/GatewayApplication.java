package br.com.alurafood.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
@EnableEurekaClient
//@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();

		for (DotenvEntry entry : dotenv.entries())
			System.setProperty(entry.getKey(), entry.getValue());
		
		SpringApplication.run(GatewayApplication.class, args);
	}

}
