package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}