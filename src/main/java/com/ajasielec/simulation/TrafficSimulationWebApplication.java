package com.ajasielec.simulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TrafficSimulationWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrafficSimulationWebApplication.class, args);
    }

}
