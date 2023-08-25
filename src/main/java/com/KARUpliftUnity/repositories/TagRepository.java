package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByPost(Post post);

    List<Tag> findAllByTagContainsIgnoreCase(String query);

}