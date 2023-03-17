package com.gourmetoven.gourmetapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "GourmetOven App", version = "1.0.0", description = "GourmetOven Demo"))
public class GourmetAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(GourmetAppApplication.class, args);
    }
}
