package br.com.alurafood.avaliacao.avaliacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
public class AvaliacaoApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();

		for (DotenvEntry entry : dotenv.entries())
			System.setProperty(entry.getKey(), entry.getValue());
		
		SpringApplication.run(AvaliacaoApplication.class, args);
	}

}
