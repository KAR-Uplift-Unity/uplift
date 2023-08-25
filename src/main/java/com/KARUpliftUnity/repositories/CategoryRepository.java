package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Category;
import com.KARUpliftUnity.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category deleteByPostId (long id);

    List<Category> findAllByPost(Post post);
}