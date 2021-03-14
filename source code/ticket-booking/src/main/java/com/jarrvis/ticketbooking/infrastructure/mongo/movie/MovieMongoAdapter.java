package com.jarrvis.ticketbooking.infrastructure.mongo.movie;

import com.jarrvis.ticketbooking.domain.Movie;
import com.jarrvis.ticketbooking.domain.MovieRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MovieMongoAdapter implements MovieRepository {

    private final MovieMongoRepository movieMongoRepository;

    public MovieMongoAdapter(MovieMongoRepository movieMongoRepository) {
        this.movieMongoRepository = movieMongoRepository;
    }

    @Override
    public Flux<Movie> findAll() {
        return this.movieMongoRepository.findAll()
                .flatMap(movieDocument ->
                        Mono.just(new Movie(movieDocument.getName(), movieDocument.getDescription(), movieDocument.getDuration())));
    }

    @Override
    public Mono<Movie> findByName(String name) {
        return this.movieMongoRepository.findByName(name)
                .flatMap(movieDocument ->
                        Mono.just(new Movie(movieDocument.getName(), movieDocument.getDescription(), movieDocument.getDuration())));
    }

    @Override
    public Mono<Movie> save(Movie movie) {
        MovieDocument document = new MovieDocument(movie.getName(), movie.getDescription(), movie.getDuration());
        return this.movieMongoRepository.save(document)
                .flatMap(entity -> Mono.just(entity.mutateTo()));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return this.movieMongoRepository.existsByName(name);
    }
}
