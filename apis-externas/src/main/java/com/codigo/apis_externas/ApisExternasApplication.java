package com.codigo.apis_externas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApisExternasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApisExternasApplication.class, args);
	}

}
