package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Category;
import com.KARUpliftUnity.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category deleteByPostId (long id);

    List<Category> findAllByPost(Post post);
}