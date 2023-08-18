package com.KARUpliftUnity.repositories;

import com.KARUpliftUnity.models.Update;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateRepository extends JpaRepository<Update, Long> {
}