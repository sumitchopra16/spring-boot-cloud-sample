package com.example.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication

/*
This annotation causes this application to register itself as eureka client with eureka server.
It will use spring.application.name property to register with eureka server.
 */
@EnableEurekaClient

/*
This annotation enables adding circuit breaker using hystrix and thus helps in enabling fault tolerance for this
application
 */
@EnableCircuitBreaker

/*
This annotation will make this application as hystrix dashboard in addition to whatever else it is doing.
Another way is to create a separate app as hysterix dashboard so that other applications which apply the circuit breaker
will publish the information to the hystrix dashboard app. For sample applicartion we are using existing app for hysterix
dashboard.
 */
@EnableHystrixDashboard
public class MovieCatalogServiceApplication {

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		/*
			Timeout of 3 seconds is set, So if response is not received in 3 seconds then timeout happens.
			So that threads ets free up. So that new requests will not have to wait for threads.
		 */
		clientHttpRequestFactory.setConnectTimeout(3000);
		return new RestTemplate(clientHttpRequestFactory);
	}

	@Bean
	public WebClient.Builder getWebClientBuilder(){
		return WebClient.builder();
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}
