package com.example.moviecatalogservice.controllers;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import com.example.moviecatalogservice.services.CatalogService;
import com.example.moviecatalogservice.services.RatingService;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private RatingService ratingService;



    /*@Autowired
    *//*
        This can be used to get the instances of a service. In scenario when multiple instances of a services are running
        you can get the list of instances and chose the instance to delegate the request to.
     *//*
    private DiscoveryClient discoveryClient;*/

    @RequestMapping("/{userId}")

    /*
        This annotation tells the spring to create a proxy class for this class so that hysterix can manage the
        functioning of this method. The proxy class will contain the Circuit Breaker Logic. So when a remote call is made
        from a function, hysterix is monitoring it. If hysterix finds the calls as faulty as per the configured
        parameters then it will call the fallback method by breaking the circuit.

        This annotation is commented from here, as this method is divided into 2 separate methods, each containing
        a rest call to other microservice.
     */
    //@HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

      /* UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId, UserRating.class);
        //ParameterizedType<List<Rating>> ratingType =

       return userRating.getUserRatings().stream().map(rating -> {
           Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);

           *//*
           Movie movie = webClientBuilder.build()
                   .get()
                   .uri("http://localhost:8082/movies/"+rating.getMovieId())
                   .retrieve()
                   .bodyToMono(Movie.class)//Mono is a way of saying that you are going to get this object in the future. as this is asynchronous call. ie you are getting back an asynchronous object. Mono is the empty container, which will be returned back  when it gets filled .
                   .block(); // This line will block the execution until we get back the object in the mono. As next line is asynchronous and depends on the movie object.
            *//*

           return new CatalogItem(movie.getName(), "Test", rating.getRating());
       }).collect(Collectors.toList());*/


        // We need to put getUserRating & getCatalogItem methods containing @hysterixCommand annotation on different methods
        // so that proxy can be used to call these methods
       /* UserRating userRating = getUserRating(userId);
        return userRating.getRatings().stream().map(rating -> getCatalogItem(rating))
                .collect(Collectors.toList());*/

        UserRating userRating = ratingService.getUserRating(userId);
        return userRating.getRatings().stream().map(rating -> catalogService.getCatalogItem(rating))
                .collect(Collectors.toList());

    }




   /* public List<CatalogItem> getFallbackCatalog(String userId){
        return Arrays.asList(new CatalogItem("No Movie", "", 0));
    }*/

}
