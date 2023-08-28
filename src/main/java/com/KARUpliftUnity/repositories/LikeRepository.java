package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Like;
import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndPost(User user, Post post);

    List<Like> findAllByUser(User user);

    List<Like> findAllByPost(Post post);
    int countByPost(Post post);
}