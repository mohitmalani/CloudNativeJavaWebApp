package com.example.healthcheckapi.repository;

import com.example.healthcheckapi.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUserId(String userId);
}
