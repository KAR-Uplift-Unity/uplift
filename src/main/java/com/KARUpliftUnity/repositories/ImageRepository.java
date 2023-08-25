package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Image;
import com.KARUpliftUnity.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPost(Post post);
    List<Image> findAllByPost(Post post);
}