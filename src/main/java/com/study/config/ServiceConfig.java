package com.study.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@Configuration
public class ServiceConfig {

	@Bean
	@LoadBalanced
	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, 
			OAuth2ClientContext clientContext) {
		return new OAuth2RestTemplate(resource, clientContext);
	}
	
}
