package com.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController
@EnableCircuitBreaker
@EnableOAuth2Sso
public class ApplicationController extends WebSecurityConfigurerAdapter{
	
	/*@Inject
	RestTemplate restTemplate;*/
	
	@Autowired
	private OAuth2ClientContext clientContext;
	
	@Autowired
	private OAuth2ProtectedResourceDetails resource;
	
	@Autowired
	OAuth2RestTemplate oauth2RestTemplate;
	
	@Bean
	@LoadBalanced
	public OAuth2RestTemplate oauth2RestTemplate() {
		return new OAuth2RestTemplate(resource, clientContext);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/login**").permitAll().anyRequest().authenticated();
	}
	
	/*@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}*/
	
	@HystrixCommand(fallbackMethod="noScore")
	@RequestMapping(value="/score", method=RequestMethod.GET)
	public String getScore() {
		
		OAuth2AccessToken token = clientContext.getAccessToken();
		System.out.println("Token->" + token);
		
		String url = "http://scorecardservice/getScore";
		ResponseEntity<String> entity = oauth2RestTemplate.exchange(url, HttpMethod.GET, null, String.class);
		return entity.getBody();
	}

	public String noScore() {
		return "Bad conditions @Stadium is forcing us to a delayed/no feed";
	}
	
	//below method to get the services via ribbon and without discovery
	/*@RequestMapping(value="/playerProfile/{id}", method=RequestMethod.GET)
	public String playerProfile(@PathVariable String id) {
		ResponseEntity<String> response = oauth2RestTemplate.getForEntity("http://playerProfile/player/" + id, String.class);
		return response.getBody();
	}*/
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String normalPage() {
		return "This is not required to be authorized";
	}
	
	
}
