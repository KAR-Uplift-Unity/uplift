package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.Update;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpdateRepository extends JpaRepository<Update, Long> {
    List<Update> findAllByPost(Post post);
}