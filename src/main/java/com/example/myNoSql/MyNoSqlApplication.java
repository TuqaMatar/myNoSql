package com.example.myNoSql;

import com.example.myNoSql.model.BootStrappingNode;
import com.example.myNoSql.service.AuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MyNoSqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNoSqlApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public BootStrappingNode bootstrapNode() {
		List<String> nodeIpAddresses = Arrays.asList("192.168.1.2", "192.168.1.3", "192.168.1.4"); // Replace with actual IP addresses
		return new BootStrappingNode(nodeIpAddresses);
	}

	@Bean
	public AuthService authService() {
		return new AuthService();
	}
}
