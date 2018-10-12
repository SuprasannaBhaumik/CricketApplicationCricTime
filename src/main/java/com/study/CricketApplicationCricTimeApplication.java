package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name="playerProfile")
public class CricketApplicationCricTimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CricketApplicationCricTimeApplication.class, args);
	}
}
