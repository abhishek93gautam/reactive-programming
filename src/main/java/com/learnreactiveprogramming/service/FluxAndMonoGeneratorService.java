package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("abhi","dravu","kullu"))
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Abhishek")
                .log();
    }

    public Flux<String> namesFlux_map(int stringLength) {

        return Flux.fromIterable(List.of("abhi","dravu","kullu"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s)
                .doOnNext(name -> {
                    System.out.println("Name is: " + name);
                    name.toLowerCase();
                })
                .doOnSubscribe(s -> {
                    System.out.println("Subscription is: "+ s);
                })
                .doOnComplete(() -> {
                    System.out.println("Inside the complete callback");
                })
                .doFinally(signalType -> {
                    System.out.println("Inside Finally: "+ signalType);
                })
                .log();
    }

    public Flux<String> namesFlux_flapMap(int stringLength) {

        return Flux.fromIterable(List.of("abhi","dravu","kullu"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> namesFlux_flapMap_async(int stringLength) {

        return Flux.fromIterable(List.of("abhi","dravu","kullu"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> namesFlux_concatMap(int stringLength) {

        return Flux.fromIterable(List.of("abhi","dravu","kullu"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                                .filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("abhi","dravu","kullu"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString);

        var defaultFlux = Flux.just("default")
                .transform(filterMap);

        return Flux.fromIterable(List.of("abhi","dravu","kullu"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay(String name) {
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> namesFlux_immutable() {

        var namesFlux = Flux.fromIterable(List.of("abhi","dravu","kullu"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .defaultIfEmpty("default")
                .log();
    }

    public Mono<String> namesMono_map_filter_switchIfEmpty(int stringLength) {

        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .switchIfEmpty(Mono.just("default"))
                .log();
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> exploreConcat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux)
                .log();
    }

    public Flux<String> explore_concatWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> explore_concatWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.concatWith(bMono).log();
    }

    public Flux<String> exploreMerge() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux)
                .log();
    }

    public Flux<String> exploreMergeWith() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux)
                .log();
    }

    public Flux<String> exploreMergeWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.mergeWith(bMono)
                .log();
    }

    public Flux<String> explore_merge_sequential() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux, defFlux)
                .log();
    }

    public Flux<String> explore_zip() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second)
                .log();
    }

    public Flux<String> explore_zip_1() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log();
    }

    public Flux<String> explore_zipWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second)
                .log();
    }

    public Mono<String> exploreZipWith_mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log();
    }

    public Flux<String> exceptionFlux() {

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred.")))
                .concatWith(Flux.just("D"))
                .log();
    }

    public Flux<String> explore_OnErrorReturn() {

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("Exception Occurred.")))
                .onErrorReturn("D")
                .log();
    }

    public Flux<String> explore_OnErrorResume(Exception e) {

        var recoveryFlux = Flux.just("D", "E", "F");

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    log.error("Exception is: " + ex);
                    if (ex instanceof IllegalStateException)
                        return recoveryFlux;
                    else
                        return Flux.error(ex);
                })
                .log();
    }

    public Flux<String> explore_OnErrorContinue() {

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if (name.equals("B"))
                        throw new IllegalStateException("Exception Occurred");
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorContinue((ex, name) -> {
                    log.error("Exception is ", ex);
                    log.info("Name is {}", name);
                })
                .log();
    }

    public Flux<String> explore_OnErrorMap() {

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if (name.equals("B"))
                        throw new IllegalStateException("Exception Occurred");
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorMap((ex) -> {
                    log.error("Exception is ", ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    public Flux<String> explore_doOnError() {

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("Exception Occurred.")))
                .doOnError(ex -> {
                    log.error("Exception is ", ex);
                })
                .log();
    }

    public Mono<Object> explore_mono_onErrorReturn() {

        return Mono.just("A")
                .map(value -> {
                    throw new RuntimeException("Exception Occurred");
                })
                .onErrorReturn("ABC")
                .log();
    }

    public Flux<Integer> explore_generate() {

        return Flux.generate(() -> 1, (state, sink) -> {
            sink.next(state * 2);

            if (state == 10 ) {
                sink.complete();
            }

            return state + 1;
        });
    }

    public static List<String> names() {
        delay(1000);
        return List.of("Abhi", "Dravu", "Kullu");
    }

    public Flux<String> explore_create() {

        return Flux.create(sink -> {
            names().forEach(sink::next);
            sink.complete();
        });
    }

    public Flux<String> explore_create_completableFuture() {

        return Flux.create(sink -> {
            CompletableFuture
                    .supplyAsync(FluxAndMonoGeneratorService::names)
                    .thenAccept(names -> {
                        names.forEach((name) -> {
                            sink.next(name);
                            sink.next(name);
                        });
                    })
                    .thenRun(() -> sendEvents(sink));
        });
    }

    public void sendEvents(FluxSink<String> sink) {

        CompletableFuture
                .supplyAsync(FluxAndMonoGeneratorService::names)
                .thenAccept(names -> {
                    names.forEach(sink::next);
                })
                .thenRun(sink::complete);
    }

    public Mono<String> explore_create_mono() {
        return Mono.create(sink -> {
            sink.success("alex");
        });
    }

    public Flux<String> explore_handle() {

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .handle((name, sink) -> {
                    if (name.length() > 3) {
                        sink.next(name.toUpperCase());
                    }
                });
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is: " + name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> {
                    System.out.println("Mono name :" + name);
                });
    }
}
