package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieInfoServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MovieInfoService movieInfoService =
            new MovieInfoService(webClient);

    @Test
    void retrieveAllMovieInfo_RestClient() {

        var movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();

        StepVerifier.create(movieInfoFlux)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    void retrieveMovieInfoById_RestClient() {

        Long id = 1L;
        var movieInfoMono = movieInfoService.retrieveMovieInfoById_RestClient(id);

        StepVerifier.create(movieInfoMono)
                .assertNext(movie -> {
                    assertEquals(movie.getName(), "Batman Begins");
                })
                .verifyComplete();
    }
}