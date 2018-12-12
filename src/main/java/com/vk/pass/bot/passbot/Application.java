package com.vk.pass.bot.passbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        Server server = context.getBean("server", Server.class);
        server.start();
    }
}
