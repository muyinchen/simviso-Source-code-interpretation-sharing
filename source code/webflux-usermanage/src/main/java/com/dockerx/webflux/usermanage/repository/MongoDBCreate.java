package com.dockerx.webflux.usermanage.repository;

import com.dockerx.webflux.usermanage.domain.Role;
import com.dockerx.webflux.usermanage.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/7 1:06.
 */
@Component
@Slf4j
public class MongoDBCreate implements CommandLineRunner {
    private static final User defaultAdmin = new User("", "admin",
            "{bcrypt}$2a$10$y3WZ2gUVsYkgTurMIIDQueSKtEI3nQtUl6Y4VH3vk0izY8gFcfexe",
            "Default", "Admin", Role.ADMIN, true);

    private final UserRepository userRepository;

    @Autowired
    public MongoDBCreate(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
       /* userRepository.count()
                      .doOnNext(count -> {
                          log.info("MongoDB started successfully. Users count: " + count);
                          if (count == 0) createDefaultAdminUser();
                      })
                      .doOnError(error ->
                              log.warn("Can't connect to MongoDB. Did you forget to start it with 'mongod --dbpath=<path-to-mongo-data-directory>'?"))
                      .subscribe();*/
    }

    private void createDefaultAdminUser() {
        userRepository.save(defaultAdmin)
                      .subscribe(user -> log.info("Users not found - adding default Admin user: " + user));
    }
}
