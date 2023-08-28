package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Comment;
import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByIdAsc(Post post);

    List<Comment> findAllByUser(User user);

    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByFlaggedIsTrue();

    Comment findById(long id);
}