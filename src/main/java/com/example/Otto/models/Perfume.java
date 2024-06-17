package com.example.Otto.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Perfume class represents a perfume entity in the database.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 13.06.2024
 */
@Entity
@Table(name = "Perfume")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Perfume {
    /**
     * The unique identifier for the perfume.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * The name of the perfume.
     */
    @Column(name = "name")
    private String name;

    /**
     * The description of the perfume.
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * The producer of the perfume.
     */
    @Column(name = "producer")
    private String producer;

    /**
     * The price of the perfume.
     */
    @Column(name = "price")
    private double price;

    /**
     * The volume of the perfume.
     */
    @Column(name = "volume")
    private int volume;

    /**
     * The amount of the perfume.
     */
    @Column(name = "amount")
    private int amount;

    /**
     * The sex of the user.
     */
    @Column(name = "sex")
    private String sex;

    /**
     * The preferred aroma of the user.
     */
    @Column(name = "aroma")
    private String aroma;

    /**
     * The preferred saturation level of the user.
     */
    @Column(name = "saturation")
    private String saturation;

    /**
     * The preferred time of day for using perfume by the user.
     */
    @Column(name = "DayTime")
    private String dayTime;

    /**
     * The specificity of perfume usage for the user.
     */
    @Column(name = "specificity")
    private String specificity;

    /**
     * The preferred season for using perfume by the user.
     */
    @Column(name = "season")
    private String season;

    /**
     * The list of images associated with the perfume.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "perfume", orphanRemoval = true)
    private List<Image> images = new ArrayList<>(3);

    /**
     * The ID of the preview image associated with the perfume.
     */
    private Long previewImageId;

    /**
     * The date and time when the perfume was created.
     */
    private LocalDateTime dateOfCreated;

    /**
     * Initializes the dateOfCreated field with the current date and time before persisting the entity.
     */
    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    /**
     * Adds an image to the list of images associated with the perfume.
     *
     * @param image The image to add.
     */
    public void addImage(int index, Image image) {
        image.setPerfume(this);
        images.add(index, image);
    }

    private final static String[] sexes = {"Чоловічі", "Жіночі", "Унісекс"};
    private final static String[] aromas = {"Квіткові", "Цитрусові", "Східні", "Деревні", "Фужерні"};
    private final static String[] dayTimes = {"Ранок", "День", "Вечір"};
    private final static String[] saturations = {"Легкі", "Насичені", "Помірні"};
    private final static String[] seasons = {"Літо", "Осінь", "Зима", "Весна"};
    private final static String[] specificities = {"Повсякденні", "Спеціальні", "Робота", "Відпочинок"};

    public static String[] getSexes() {
        return sexes;
    }

    public static String[] getAromas() {
        return aromas;
    }

    public static String[] getDayTimes() {
        return dayTimes;
    }

    public static String[] getSaturations() {
        return saturations;
    }

    public static String[] getSeasons() {
        return seasons;
    }

    public static String[] getSpecificities() {
        return specificities;
    }
}
