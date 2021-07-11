package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoSchedulersServiceTest {

    FluxAndMonoSchedulersService fluxAndMonoSchedulersService
            = new FluxAndMonoSchedulersService();

    @Test
    void explorePublishOn() {

        var flux = fluxAndMonoSchedulersService.explorePublishOn();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void exploreSubscribeOn() {

        var flux = fluxAndMonoSchedulersService.exploreSubscribeOn();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel() {

        var flux = fluxAndMonoSchedulersService.explore_parallel();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatMap() {

        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatMap();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatMap_merge() {

        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatMap_merge();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatMapSequential() {
        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatMapSequential();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }
}