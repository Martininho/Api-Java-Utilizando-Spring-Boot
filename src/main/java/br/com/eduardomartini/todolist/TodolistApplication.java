package br.com.eduardomartini.todolist;

import org.springframework.boot.SpringApplication;                       
import org.springframework.boot.autoconfigure.SpringBootApplication; /* Import.nome do pacote.nome da classe que utilizamos */

@SpringBootApplication    /* Classe principal da aplicação */
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

}
