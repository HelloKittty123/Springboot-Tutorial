package com.tutorial.apidemo.Springboot.Tutorial.database;

import com.tutorial.apidemo.Springboot.Tutorial.model.Product;
import com.tutorial.apidemo.Springboot.Tutorial.repositories.ProductRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Now connect with mysql using JPA
/*
docker run -d --rm --name mysql-spring-boot-tutorial \
-e MYSQL_ROOT_PASSWORD=trung123Aa \
-e MYSQL_USER=trungtd \
-e MYSQL_PASSWORD=trung123Aa \
-e MYSQL_DATABASE=test_db \
-p 3309:3306 \
--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql \
mysql:latest

mysql -h localhost -P 3309 --protocol=tcp -u trungtd -p

* */

@Configuration
public class Database {
    // logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRespository productRespository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("MacBookPro", 2020, 2400.0, "");
                Product productB = new Product("MacBookPro2", 2020, 2400.0, "");
                logger.info("insert data: " + productRespository.save(productA));
                logger.info("insert data: " + productRespository.save(productB));
            }
        };
    }
}
