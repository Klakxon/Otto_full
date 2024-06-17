package com.example.Otto.repository;

import com.example.Otto.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The ImageRepository interface provides access to the database for Image entities.
 * It extends the JpaRepository interface, which provides CRUD operations for the Image entity.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

}
