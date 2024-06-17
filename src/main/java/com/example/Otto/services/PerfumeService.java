package com.example.Otto.services;

import com.example.Otto.models.Image;
import com.example.Otto.models.Perfume;
import com.example.Otto.models.User;
import com.example.Otto.models.enums.*;
import com.example.Otto.repository.ImageRepository;
import com.example.Otto.repository.PerfumeRepository;
import com.example.Otto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The PerfumeService class provides services related to Perfume entities.
 * It handles operations such as retrieving perfumes, saving new perfumes, and deleting existing perfumes.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 11.06.2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    /**
     * Retrieves a list of perfumes optionally filtered by name.
     *
     * @param name the name of the perfume to filter by (can be null)
     * @return Iterable<Perfume> list of perfumes
     */
    public Iterable<Perfume> getPerfumes(String name) {
        if (name != null && !name.isEmpty()) {
            return perfumeRepository.findByNameContaining(name);
        }
        return perfumeRepository.findAll();
    }

    /**
     * Saves a new perfume along with associated images.
     *
     * @param perfume   the perfume to save
     * @param file1     the first image file
     * @param file2     the second image file
     * @param file3     the third image file
     * @throws IOException if an I/O error occurs while processing the image files
     */
    public void savePerfume(Perfume perfume, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            perfume.addImage(0, image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            perfume.addImage(1, image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            perfume.addImage(2, image3);
        }
        log.info("Saving new Perfume. Name: {}; Producer: {}; Price: {}", perfume.getName(), perfume.getProducer(), perfume.getPrice());
        Perfume perfumeFromDb = perfumeRepository.save(perfume);
        perfumeFromDb.setPreviewImageId(perfumeFromDb.getImages().get(0).getId());
        perfumeRepository.save(perfume);
    }

    /**
     * Retrieves the user associated with the provided principal.
     *
     * @param principal the principal object representing the current authenticated user
     * @return User object representing the user associated with the principal
     */
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    /**
     * Converts a multipart file to an Image entity.
     *
     * @param file the multipart file to convert
     * @return Image entity representing the converted multipart file
     * @throws IOException if an I/O error occurs while processing the file
     */
    public Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    /**
     * Deletes a perfume by its ID.
     *
     * @param id the ID of the perfume to delete
     */
    public void deletePerfume(Long id) {
        perfumeRepository.deleteById(id);
    }

    /**
     * Retrieves a perfume by its ID.
     *
     * @param id the ID of the perfume to retrieve
     * @return Perfume object representing the retrieved perfume
     */
    public Perfume getPerfumeById(Long id) {
        return perfumeRepository.findById(id).orElse(null);
    }

    public Iterable<Perfume> listAllSortedByPrice(Iterable<Perfume> perfumes, String sortMethod) {
        if (perfumes != null && sortMethod != null) {
            return StreamSupport.stream(perfumes.spliterator(), false)
                    .sorted(sortMethod.equals("priceDesc") ? Comparator.comparing(Perfume::getPrice).reversed() : Comparator.comparing(Perfume::getPrice))
                    .collect(Collectors.toList());
        }
        return perfumes;
    }

    public Iterable<Perfume> listAllSortedByName(Iterable<Perfume> perfumes, String sortMethod) {
        if (perfumes != null && sortMethod != null) {
            return StreamSupport.stream(perfumes.spliterator(), false)
                    .sorted(sortMethod.equals("nameDesc") ? Comparator.comparing(Perfume::getName).reversed() : Comparator.comparing(Perfume::getName))
                    .collect(Collectors.toList());
        }
        return perfumes;
    }

    public Iterable<String> listAllProducers() {
        Stream<String> producersStream = perfumeRepository.findAllProducers().stream();
        return producersStream.sorted().collect(Collectors.toList());
    }

    public Iterable<Integer> listAllVolumes() {
        Stream<Integer> volumesStream = StreamSupport.stream(perfumeRepository.findAllVolumes().spliterator(), false);
        return volumesStream.sorted().collect(Collectors.toList());
    }

    public String[] listAllSexes() {
        return Perfume.getSexes();
    }

    public String[] listAllSaturations() {
        return Perfume.getSaturations();
    }

    public String[] listAllSeasons() {
        return Perfume.getSeasons();
    }

    public String[] listAllDayTimes() {
        return Perfume.getDayTimes();
    }

    public String[] listAllAromas() {
        return Perfume.getAromas();
    }

    public String[] listAllSpecificities() {
        return Perfume.getSpecificities();
    }

    public Iterable<Perfume> filterPerfumes(
            Iterable<Perfume> perfumes,
            List<String> producers,
            Double priceMin,
            Double priceMax,
            List<Integer> volumes,
            List<String> sexes,
            List<String> aromas,
            List<String> saturations,
            List<String> dayTimes,
            List<String> specificities,
            List<String> seasons) {

        return StreamSupport.stream(perfumes.spliterator(), false)
                .filter(perfume ->
                        (producers == null || producers.isEmpty() || producers.contains(perfume.getProducer())) &&
                                (priceMin == null || perfume.getPrice() >= priceMin) &&
                                (priceMax == null || perfume.getPrice() <= priceMax) &&
                                (volumes == null || volumes.isEmpty() || volumes.contains(perfume.getVolume())) &&
                                (sexes == null || sexes.isEmpty() || sexes.contains(perfume.getSex())) &&
                                (aromas == null || aromas.isEmpty() || aromas.contains(perfume.getAroma())) &&
                                (saturations == null || saturations.isEmpty() || saturations.contains(perfume.getSaturation())) &&
                                (dayTimes == null || dayTimes.isEmpty() || dayTimes.contains(perfume.getDayTime())) &&
                                (specificities == null || specificities.isEmpty() || specificities.contains(perfume.getSpecificity())) &&
                                (seasons == null || seasons.isEmpty() || seasons.contains(perfume.getSeason())))
                .collect(Collectors.toList());
    }

    public Perfume updatePerfume(Long id, Perfume updatedPerfume, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Perfume perfume = getPerfumeById(id);
        if (perfume != null) {
            // Update perfume fields
            if (!updatedPerfume.getName().isEmpty()) perfume.setName(updatedPerfume.getName());
            if (!updatedPerfume.getProducer().isEmpty()) perfume.setProducer(updatedPerfume.getProducer());
            if (updatedPerfume.getPrice() >= 0) perfume.setPrice(updatedPerfume.getPrice());
            if (updatedPerfume.getVolume() >= 0) perfume.setVolume(updatedPerfume.getVolume());
            if (updatedPerfume.getAmount() >= 0) perfume.setAmount(updatedPerfume.getAmount());
            perfume.setDescription(updatedPerfume.getDescription());
            if (!updatedPerfume.getSex().isEmpty()) perfume.setSex(updatedPerfume.getSex());
            if (!updatedPerfume.getAroma().isEmpty()) perfume.setAroma(updatedPerfume.getAroma());
            if (!updatedPerfume.getSaturation().isEmpty()) perfume.setSaturation(updatedPerfume.getSaturation());
            if (!updatedPerfume.getDayTime().isEmpty()) perfume.setDayTime(updatedPerfume.getDayTime());
            if (!updatedPerfume.getSpecificity().isEmpty()) perfume.setSpecificity(updatedPerfume.getSpecificity());
            if (!updatedPerfume.getSeason().isEmpty()) perfume.setSeason(updatedPerfume.getSeason());

            // Handle image files
            if (!file1.isEmpty()) {
                Image image1 = toImageEntity(file1);
                image1.setPreviewImage(true);
                setImage(0, image1, perfume);
            }
            if (!file2.isEmpty()) {
                Image image2 = toImageEntity(file2);
                setImage(1, image2, perfume);
            }
            if (!file3.isEmpty()) {
                Image image3 = toImageEntity(file3);
                setImage(2, image3, perfume);
            }

            // Save perfume
            Perfume perfumeFromDb = perfumeRepository.save(perfume);
            if (!perfumeFromDb.getImages().isEmpty()) {
                perfumeFromDb.setPreviewImageId(perfumeFromDb.getImages().get(0).getId());
            }
            return perfumeRepository.save(perfumeFromDb);
        }
        return null;
    }

    private void setImage(int index, Image image, Perfume perfume) {
        image.setPerfume(perfume);
        List<Image> images = perfume.getImages();

        if (images.size() > index) {
            Image existingImage = images.get(index);
            if (existingImage != null) {
                imageRepository.delete(existingImage); // Видалення дійсного зображення з репозиторію
                images.set(index, image); // Оновлення зображення в списку
            }
        } else {
            // Якщо індекс виходить за межі поточного списку, то додаємо нове зображення
            images.add(image);
        }
    }
}
