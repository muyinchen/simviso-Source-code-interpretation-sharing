package com.dockerx.webflux.usermanage.repository;

import com.dockerx.webflux.usermanage.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/7 1:09.
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String> {
    Flux<User> findByUsername(final String username);
    Mono<User> findOneByUsername(final String username);
}
