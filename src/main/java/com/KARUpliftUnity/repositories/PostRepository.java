package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post getIdByTitle(String title);
}