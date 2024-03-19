package br.com.bjbraz.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionListenerApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(TransactionListenerApplication.class, args);

	}
}
