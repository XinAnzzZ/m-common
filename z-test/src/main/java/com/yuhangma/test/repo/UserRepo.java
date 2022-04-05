package com.yuhangma.test.repo;

import com.yuhangma.test.repo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/2/10
 */
public interface UserRepo extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM user WHERE password = :pd FOR UPDATE ", nativeQuery = true)
    Optional<User> selectForUpdate(@Param("pd") String password);

    void deleteByPassword(String password);
}
