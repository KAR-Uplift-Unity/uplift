package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Like;
import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndPost(User user, Post post);
    int countByPost(Post post);;
}