package com.dockerx.webflux.usermanage.handlers;

import com.dockerx.webflux.usermanage.domain.Role;
import com.dockerx.webflux.usermanage.domain.User;
import com.dockerx.webflux.usermanage.exceptions.CustomValidationException;
import com.dockerx.webflux.usermanage.exceptions.UserDataException;
import com.dockerx.webflux.usermanage.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/8 21:27.
 */
@Component
@Slf4j
public class UserHandler {
    private final UserRepository userRepo;

    private final Validator validator;

    @Autowired
    public UserHandler(UserRepository userRepo, @Qualifier("defaultValidator") Validator validator) {
        this.userRepo = userRepo;
        this.validator = validator;
    }

    public Mono<ServerResponse> findAllUsers(ServerRequest request) {

        return ok().contentType(MediaType.APPLICATION_JSON)
                   .body(userRepo.findAll(), User.class);
    }

    public Mono<ServerResponse> findUser(ServerRequest request) {
        String id = request.pathVariable("id");
        if (isValidId(id)) {
            return badRequest().syncBody("Error: Invalid URL user id: " + id);
        }
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return userRepo.findById(id).flatMap(user ->
                ok().contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(user))).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.body(BodyExtractors.toMono(User.class))
                      .filterWhen(user -> userRepo.findOneByUsername(user.getUsername())
                                                  .doOnNext(foundUser -> log.debug("Username exists: " + foundUser.getUsername()))
                                                  .hasElement()
                                                  .map(success -> !success)
                      ).map(user -> {
                    DataBinder binder = new DataBinder(user);
                    binder.setValidator(validator);
                    binder.validate();
                    if (binder.getBindingResult().hasErrors()) {
                        log.error(binder.getBindingResult().getAllErrors().toString());
                        throw new CustomValidationException(binder.getBindingResult().getAllErrors());
                    }
                    return user;
                }).flatMap(user -> {
                    if (user.getRole() == null) {
                        user.setRole(Role.CUSTOMER);
                    }
                    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setActive(true);
                    return userRepo.save(user);
                }).flatMap(createdUser -> {
                    log.debug("New user saved: " + createdUser);
                    return created(
                            URI.create(request.path() + "/" + createdUser.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .syncBody(createdUser);
                }).switchIfEmpty(badRequest().syncBody("Error: Username already registered."));
    }

    public Mono<ServerResponse> editUser(ServerRequest request) {
        String id = request.pathVariable("id");
        if (isValidId(id)) {
            return badRequest().syncBody("Error: Invalid URL user id: " + id);
        }

        return request.body(BodyExtractors.toMono(User.class))
                      .zipWith(userRepo.findById(Mono.justOrEmpty(id)))
                      .flatMap((Tuple2<User, User> tuple) -> {
                          User reqUser = tuple.getT1();
                          User foundUser = tuple.getT2();
                          //exclude exceptions before performing normal business
                          //Determine if there is any need to modify the user ID and user name. These two cannot be modified.
                          if (!foundUser.getId().equals(reqUser.getId()) || !foundUser.getUsername()
                                                                                      .equals(reqUser.getUsername())) {
                              throw new UserDataException("Username and Id can not be changed: "
                                      + foundUser.getUsername() + " - " + foundUser.getId());
                          }
                          User resultUser = User.builder() // merging user data
                                                .id(foundUser.getId())
                                                .username(foundUser.getUsername())
                                                .password(foundUser.getPassword()) // password change should be requested separately
                                                .fname(reqUser.getFname() != null && reqUser.getFname().length() > 0 ?
                                                        reqUser.getFname() : foundUser.getFname())
                                                .lname(reqUser.getLname() != null && reqUser.getLname().length() > 0 ?
                                                        reqUser.getLname() : foundUser.getLname())
                                                .role(reqUser.getRole() != null ?
                                                        reqUser.getRole() : foundUser.getRole())
                                                .active(reqUser.isActive())
                                                .build();
                          log.debug("About to edit User: " + resultUser);
                          return userRepo.save(resultUser);

                      })
                      .log()
                      .flatMap(editedUser -> ok().contentType(MediaType.APPLICATION_JSON).syncBody(editedUser))
                      .switchIfEmpty(badRequest().syncBody("Error: User does not exist and can not be edited."))
                      .subscribeOn(Schedulers.elastic());


    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        if (isValidId(id)) return badRequest().syncBody("Error: Invalid URL user id: " + id);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return userRepo.findById(id)
                       .flatMap(user -> userRepo.deleteById(id)
                                                .doOnEach(signal -> log.debug("USER DELETED: " + user))
                                                .then(ok().contentType(MediaType.APPLICATION_JSON).syncBody(user)))
                       .switchIfEmpty(notFound);
    }

    private boolean isValidId(String id) {
        return id == null || id.length() != 24;
    }
}
