package com.reactive.programing.springbootreactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.reactive.programing.springbootreactor.app.models.User;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> users = new ArrayList<>();
        users.add("Alex Londoño");
        users.add("Emiliana Londoño");
        users.add("Alex Londoño");
        users.add("Alejandro Londoño");
        users.add("Aracelly Espejo");

        Flux<String> names_2 = Flux.just("Alex Londoño",
                "Emiliana Londoño",
                "Sacha Londoño ",
                "Alejandro Londoño ", "Aracelly Espejo");

        Flux<String> names =  Flux.fromIterable(users);

        names.map(name -> new User(name.split(" ")[0].toUpperCase(),
                        name.split(" ")[1].toUpperCase()))
                .filter(user -> user.getLastName().toLowerCase().equalsIgnoreCase("ESPEJO"))
                .doOnNext(user -> {
                    if (user==null)
                        throw new RuntimeException("Names can´t be empty");

                    System.out.println(user.getName().concat(" ").concat(user.getLastName()));
                })
                .map(user -> {
                    String name = user.getName().toLowerCase();
                    user.setName(name);
                    return user;
                });

        names.subscribe(log::info,
                error -> log.error(error.getMessage()),
                () -> log.info("You have successfully completed the execution of the observable"));
    }
}
