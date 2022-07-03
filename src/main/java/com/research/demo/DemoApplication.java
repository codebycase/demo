package com.research.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) throws Exception {
    ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
    SleuthTraceWithSpringRabbit rabbit = context.getBean(SleuthTraceWithSpringRabbit.class);
    rabbit.testSleuthOtelTraceWithSpringRabbit();
  }

}
