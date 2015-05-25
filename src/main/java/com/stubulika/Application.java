package com.stubulika;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    @Bean
    public Map<StubRequest, StubResponse> storeMap(){
        Map<StubRequest, StubResponse> store  = new HashMap<>();
        return store;
    }


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        logger.debug("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            logger.debug(beanName);
        }
    }

}
    

