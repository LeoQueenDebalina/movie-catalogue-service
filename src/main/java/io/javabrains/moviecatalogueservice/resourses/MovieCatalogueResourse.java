package io.javabrains.moviecatalogueservice.resourses;

import io.javabrains.moviecatalogueservice.models.CatalogueItem;
import io.javabrains.moviecatalogueservice.models.Movie;
import io.javabrains.moviecatalogueservice.models.Rating;
import io.javabrains.moviecatalogueservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController

public class MovieCatalogueResourse {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBulder;
    @GetMapping("/catalogue/{userId}")
    public List<CatalogueItem> getCatalogue(@PathVariable String userId) {
        WebClient.Builder builder = WebClient.builder();
        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
//              //for each movie id call info service and get details
                    Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
                    //put them all together
                    return new CatalogueItem(movie.getName(), "Test", rating.getRating());
                })
                .collect(Collectors.toList());

    }}
//}      Movie movie = webClientBulder.build()
////                            .get()
////                            .uri("http://localhost:8081/movies/" + rating.getMovieId())
////                            .retrieve()
////                            .bodyToMono(Movie.class)
////                            .block();
