package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService
            = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {

        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier.create(namesFlux)
                .expectSubscription()
                .expectNext("abhi","dravu","kullu")
                .verifyComplete();
    }

    @Test
    void monoFlux() {

        var nameMono = fluxAndMonoGeneratorService.nameMono();

        StepVerifier.create(nameMono)
                .expectNext("Abhishek")
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {

        int stringLength = 4;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("5-DRAVU","5-KULLU")
                .verifyComplete();

    }

    @Test
    void namesFlux_immutable() {

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutable();

        StepVerifier.create(namesFlux)
                //.expectNext("ABHI","DRAVU","KULLU")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesMono_map_filter() {

        int stringLength = 3;
        var nameMono = fluxAndMonoGeneratorService.namesMono_map_filter(stringLength);

        StepVerifier.create(nameMono)
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    void namesMono_map_filter_defaultIfEmpty() {

        int stringLength = 4;
        var nameMono = fluxAndMonoGeneratorService.namesMono_map_filter(stringLength);

        StepVerifier.create(nameMono)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_flapMap() {
        int stringLength = 4;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flapMap(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("D","R","A","V","U","K","U","L","L","U")
                .verifyComplete();
    }

    @Test
    void namesFlux_flapMap_async() {
        int stringLength = 4;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flapMap_async(stringLength);

        StepVerifier.create(namesFlux)
                //.expectNext("D","R","A","V","U","K","U","L","L","U")
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        int stringLength = 4;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("D","R","A","V","U","K","U","L","L","U")
                //.expectNextCount(10)
                .verifyComplete();

    }

    @Test
    void namesFlux_concatMap_virtualTimer() {

        VirtualTimeScheduler.getOrSet();
        int stringLength = 4;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        StepVerifier.withVirtualTime(() -> namesFlux)
                .thenAwait(Duration.ofSeconds(10))
                .expectNext("D","R","A","V","U","K","U","L","L","U")
                //.expectNextCount(10)
                .verifyComplete();

    }

    @Test
    void namesMono_flatMap() {

        int stringLength = 3;
        var nameMono = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);

        StepVerifier.create(nameMono)
                .expectNext(List.of("A","L","E","X"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany() {

        int stringLength = 3;
        var nameMono = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);

        StepVerifier.create(nameMono)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {

        int stringLength = 4;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("D","R","A","V","U","K","U","L","L","U")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {

        int stringLength = 6;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        StepVerifier.create(namesFlux)
                //.expectNext("D","R","A","V","U","K","U","L","L","U")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty() {

        int stringLength = 6;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength);

        StepVerifier.create(namesFlux)
                //.expectNext("D","R","A","V","U","K","U","L","L","U")
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void namesMono_map_filter_switchIfEmpty() {

        int stringLength = 6;

        var namesFlux = fluxAndMonoGeneratorService.namesMono_map_filter_switchIfEmpty(stringLength);

        StepVerifier.create(namesFlux)
                //.expectNext("D","R","A","V","U","K","U","L","L","U")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void exploreConcat() {

        var concatFlux = fluxAndMonoGeneratorService.exploreConcat();
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith() {
        var concatFlux = fluxAndMonoGeneratorService.explore_concatWith();
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith_mono() {

        var concatFlux = fluxAndMonoGeneratorService.explore_concatWith_mono();
        StepVerifier.create(concatFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void exploreMerge() {

        var mergeFlux = fluxAndMonoGeneratorService.exploreMerge();
        StepVerifier.create(mergeFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void exploreMergeWith() {

        var mergeFlux = fluxAndMonoGeneratorService.exploreMergeWith();
        StepVerifier.create(mergeFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void exploreMergeWith_mono() {

        var mergeFlux = fluxAndMonoGeneratorService.exploreMergeWith_mono();
        StepVerifier.create(mergeFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_merge_sequential() {

        var mergeFlux = fluxAndMonoGeneratorService.explore_merge_sequential();
        StepVerifier.create(mergeFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {

        var zipFlux = fluxAndMonoGeneratorService.explore_zip();
        StepVerifier.create(zipFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {

        var zipFlux = fluxAndMonoGeneratorService.explore_zip_1();
        StepVerifier.create(zipFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void explore_zipWith() {

        var zipFlux = fluxAndMonoGeneratorService.explore_zipWith();
        StepVerifier.create(zipFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void exploreZipWith_mono() {

        var zipMono = fluxAndMonoGeneratorService.exploreZipWith_mono();
        StepVerifier.create(zipMono)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void exceptionFlux() {

        var exceptionFlux = fluxAndMonoGeneratorService.exceptionFlux();

        StepVerifier.create(exceptionFlux)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void exceptionFlux_1() {

        var exceptionFlux = fluxAndMonoGeneratorService.exceptionFlux();

        StepVerifier.create(exceptionFlux)
                .expectNext("A", "B", "C")
                .expectErrorMessage("Exception Occurred.")
                .verify();
    }

    @Test
    void explore_OnErrorReturn() {

        var value = fluxAndMonoGeneratorService.explore_OnErrorReturn();

        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume() {

        var e = new IllegalStateException("Not a valid state");

        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume_1() {

        var e = new RuntimeException("Not a valid state");

        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_OnErrorContinue() {

        var value = fluxAndMonoGeneratorService.explore_OnErrorContinue();

        StepVerifier.create(value)
                .expectNext("A", "C", "D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorMap() {

        var value = fluxAndMonoGeneratorService.explore_OnErrorMap();

        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_doOnError() {

        var value = fluxAndMonoGeneratorService.explore_doOnError();

        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void explore_mono_onErrorReturn() {

        var value = fluxAndMonoGeneratorService.explore_mono_onErrorReturn();

        StepVerifier.create(value)
                .expectNext("ABC")
                .verifyComplete();
    }

    @Test
    void explore_generate() {

        var flux = fluxAndMonoGeneratorService.explore_generate().log();

        StepVerifier.create(flux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void explore_create() {

        var flux = fluxAndMonoGeneratorService.explore_create().log();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_create_completableFuture() {

        var flux = fluxAndMonoGeneratorService.explore_create_completableFuture().log();

        StepVerifier.create(flux)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void explore_create_mono() {

        var mono = fluxAndMonoGeneratorService.explore_create_mono();
        StepVerifier.create(mono)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    void explore_handle() {
        var flux = fluxAndMonoGeneratorService.explore_handle().log();

        StepVerifier.create(flux)
                .expectNextCount(2)
                .verifyComplete();
    }
}
