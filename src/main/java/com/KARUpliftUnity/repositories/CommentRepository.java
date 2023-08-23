package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Comment;
import com.KARUpliftUnity.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByIdAsc(Post post);

}