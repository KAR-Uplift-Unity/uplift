package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

    User findUserById(long id);
    User deleteById(long id);

    List<User> findAllByArchiveTrue();

}