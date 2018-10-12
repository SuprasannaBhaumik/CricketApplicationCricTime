package com.study.controller;

import javax.inject.Inject;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController
@EnableCircuitBreaker
public class ApplicationController {
	
	@Inject
	RestTemplate restTemplate;
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@RequestMapping(value="/score", method=RequestMethod.GET)
	@HystrixCommand(fallbackMethod="noScore")
	public String getScore() {
		String url = "http://scorecardservice/getScore";
		ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		return entity.getBody();
	}

	public String noScore() {
		return "Bad conditions @Stadium is forcing us to a delayed/no feed";
	}
	
	//below method to get the services via ribbon and without discovery
	@RequestMapping(value="/playerProfile/{id}", method=RequestMethod.GET)
	public String playerProfile(@PathVariable String id) {
		ResponseEntity<String> response = restTemplate.getForEntity("http://playerProfile/player/" + id, String.class);
		return response.getBody();
	}
	
}
