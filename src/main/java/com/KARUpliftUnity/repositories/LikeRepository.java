package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}