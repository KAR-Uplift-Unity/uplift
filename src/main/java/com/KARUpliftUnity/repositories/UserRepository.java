package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}