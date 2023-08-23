package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    Post getIdByTitle(String title);
    Post getAllByUserId (long id);
    Post findPostById(Long id);
    List<Post> findByUserIdAndArchiveFalse(Long userId);
    List<Post> findByUserIdAndArchiveTrue(Long userId);

}