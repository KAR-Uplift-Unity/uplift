package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.Tag;
import com.KARUpliftUnity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post getIdByTitle(String title);
    Post getAllByUserId(long id);
    Post findPostById(Long id);
    List<Post> findByUserIdAndArchiveFalse(Long userId);
    List<Post> findByUserIdAndArchiveTrue(Long userId);
    List<Post> findByArchiveFalse();
    List<Post> findAllByTitleContainingIgnoreCaseOrStoryContainingIgnoreCase(String query, String query1);
    List<Post> findAllByUser(User user);

}