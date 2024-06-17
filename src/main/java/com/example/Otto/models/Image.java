package com.example.Otto.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The Image class represents an image entity in the database.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@Entity
@Table(name = "image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    /**
     * The unique identifier for the image.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * The name of the image.
     */
    @Column(name = "name")
    private String name;

    /**
     * The original file name of the image.
     */
    @Column(name = "originalFileName")
    private String originalFileName;

    /**
     * The size of the image.
     */
    @Column(name = "size")
    private Long size;

    /**
     * The content type of the image.
     */
    @Column(name = "contentType")
    private String contentType;

    /**
     * Indicates whether the image is a preview image.
     */
    @Column(name = "isPreviewImage")
    private boolean isPreviewImage;

    /**
     * The byte array representing the image data.
     */
    @Lob
    @Column(name = "bytes", columnDefinition = "LONGBLOB")
    private byte[] bytes;

    /**
     * The perfume associated with the image.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Perfume perfume;
}
