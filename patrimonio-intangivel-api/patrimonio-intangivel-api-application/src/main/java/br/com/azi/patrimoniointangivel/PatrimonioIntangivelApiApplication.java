package br.com.azi.patrimoniointangivel;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(PatrimonioIntangivelProperties.class)
@SpringBootApplication
public class PatrimonioIntangivelApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatrimonioIntangivelApiApplication.class, args);
    }

}
