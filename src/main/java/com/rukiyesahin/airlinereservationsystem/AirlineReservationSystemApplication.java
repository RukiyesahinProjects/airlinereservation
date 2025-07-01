package com.rukiyesahin.airlinereservationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirlineReservationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineReservationSystemApplication.class, args);
    }

}
