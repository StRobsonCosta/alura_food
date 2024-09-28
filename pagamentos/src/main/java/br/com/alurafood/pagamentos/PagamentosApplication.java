package br.com.alurafood.pagamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class PagamentosApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();

		for (DotenvEntry entry : dotenv.entries())
			System.setProperty(entry.getKey(), entry.getValue());
		
		SpringApplication.run(PagamentosApplication.class, args);
	}

}
