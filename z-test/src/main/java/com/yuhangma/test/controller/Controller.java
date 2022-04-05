package com.yuhangma.test.controller;

import com.yuhangma.test.repo.UserRepo;
import com.yuhangma.test.repo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/14
 */
@RestController
public class Controller {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String run(long timestamp) {
        userRepo.deleteByPassword("111");
        for (int i = 0; i < 50; i++) {
            new Thread(() -> insertUser(timestamp)).start();
        }
        return "success";
    }


    private void insertUser(long timestamp) {
        while (System.currentTimeMillis() < timestamp) {

        }
        final Optional<User> user = userRepo.selectForUpdate("111");
        if (user.isPresent()) {
            throw new RuntimeException("已存在");
        }
        final User u = new User();
        final Random random = new Random(10);
        u.setUsername(String.valueOf(random.nextInt()));
        u.setPassword("111");
        userRepo.save(u);
    }

}
