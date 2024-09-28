package br.com.alurafood.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
@EnableEurekaServer
public class ServerApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();

		for (DotenvEntry entry : dotenv.entries())
			System.setProperty(entry.getKey(), entry.getValue());
		
		SpringApplication.run(ServerApplication.class, args);
	}

}
