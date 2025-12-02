package org.catalog_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org/catalog_app/entities")
@EnableJpaRepositories(basePackages = "org/catalog_app/repositories")
public class Applications {

    public static void main(String[] args) {
        SpringApplication.run(Applications.class);
    }
}