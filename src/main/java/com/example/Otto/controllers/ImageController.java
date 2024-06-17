package com.example.Otto.controllers;

import com.example.Otto.models.Image;
import com.example.Otto.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

/**
 * The ImageController class handles requests for retrieving images from the Otto application.
 * It provides an endpoint for fetching an image by its ID and returning it in the HTTP response.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@RestController
@RequiredArgsConstructor
public class ImageController {
    /**
     * Repository for accessing image data.
     */
    private final ImageRepository imageRepository;

    /**
     * Handles GET requests to retrieve an image by its ID.
     *
     * @param id the ID of the image to retrieve.
     * @return a ResponseEntity containing the image data.
     */
    @GetMapping("/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
