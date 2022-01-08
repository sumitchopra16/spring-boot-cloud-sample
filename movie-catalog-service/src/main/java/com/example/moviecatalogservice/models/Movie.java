package com.example.moviecatalogservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

/*
For marshalling and unmarshalling in java, you need an empty constructor
 */
@NoArgsConstructor
public class Movie {

    private String movieId;
    private String name;
    private String description;

}