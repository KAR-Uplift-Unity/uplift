package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}