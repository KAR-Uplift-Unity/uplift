package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}