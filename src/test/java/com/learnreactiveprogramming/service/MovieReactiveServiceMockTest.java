package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @Mock
    private MovieInfoService movieInfoService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    MovieReactiveService movieReactiveService;

    @Test
    void getAllMovies() {

        //given
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        //when
        var moviesFlux = movieReactiveService.getAllMovies();

        //then

        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getAllMovies_1() {

        //given
        var errorMessage = "Exception Occurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        //when
        var moviesFlux = movieReactiveService.getAllMovies();

        //then

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage("Exception Occurred in ReviewService")
                .verify();
    }

    @Test
    void getAllMovies_retry() {

        //given
        var errorMessage = "Exception Occurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        //when
        var moviesFlux = movieReactiveService.getAllMovies_retry();

        //then

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage("Exception Occurred in ReviewService")
                .verify();

        verify(reviewService, times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_retryWhen() {

        //given
        var errorMessage = "Exception Occurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new NetworkException(errorMessage));

        //when
        var moviesFlux = movieReactiveService.getAllMovies_retryWhen();

        //then

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage("Exception Occurred in ReviewService")
                .verify();

        verify(reviewService, times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_retryWhen_1() {

        //given
        var errorMessage = "Exception Occurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new ServiceException(errorMessage));

        //when
        var moviesFlux = movieReactiveService.getAllMovies_retryWhen();

        //then

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage("Exception Occurred in ReviewService")
                .verify();

        verify(reviewService, times(1))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_repeat() {

        //given
        var errorMessage = "Exception Occurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        //when
        var moviesFlux = movieReactiveService.getAllMovies_repeat();

        //then

        StepVerifier.create(moviesFlux)
                .expectNextCount(6)
                .thenCancel()
                .verify();

        verify(reviewService, times(6))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_repeat_n() {

        //given
        var errorMessage = "Exception Occurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        var numberOfTimes = 2L;
        //when
        var moviesFlux = movieReactiveService.getAllMovies_repeat_n(2);

        //then

        StepVerifier.create(moviesFlux)
                .expectNextCount(9)
                .verifyComplete();

        verify(reviewService, times(9))
                .retrieveReviewsFlux(isA(Long.class));
    }
}