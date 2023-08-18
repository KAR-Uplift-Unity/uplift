package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}