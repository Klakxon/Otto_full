package com.example.Otto.controllers;

import com.example.Otto.models.Perfume;
import com.example.Otto.services.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The PerfumeController class handles requests related to perfumes in the Otto application.
 * It provides endpoints for viewing, adding, and deleting perfumes, as well as displaying perfume details.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@Controller
@RequiredArgsConstructor
public class PerfumeController {

    /**
     * Service for managing perfumes.
     */
    private final PerfumeService perfumeService;
    private static final Logger logger = LoggerFactory.getLogger(PerfumeController.class);


    /**
     * Handles GET requests to the perfumes page. It displays the list of perfumes and user information.
     *
     * @param nameSearch the optional name filter for the perfumes.
     * @param model      the model to pass attributes to the view.
     * @param principal  the currently authenticated user.
     * @return the name of the view to render.
     */
    @GetMapping(value = {"/", "/perfumes"})
    public String perfumesMain(@RequestParam(name = "priceSort", required = false) String priceSort,
                               @RequestParam(name = "nameSort", required = false) String nameSort,
                               @RequestParam(name = "nameSearch", required = false) String nameSearch,
                               @RequestParam(name = "producerFilter", required = false) List<String> producers,
                               @RequestParam(name = "priceMinFilter", required = false) Double priceMin,
                               @RequestParam(name = "priceMaxFilter", required = false) Double priceMax,
                               @RequestParam(name = "volumeFilter", required = false) List<Integer> volumes,
                               @RequestParam(name = "sexFilter", required = false) List<String> sexStrings,
                               @RequestParam(name = "aromaFilter", required = false) List<String> aromaStrings,
                               @RequestParam(name = "saturationFilter", required = false) List<String> saturationStrings,
                               @RequestParam(name = "dayTimeFilter", required = false) List<String> dayTimeStrings,
                               @RequestParam(name = "specificityFilter", required = false) List<String> specificityStrings,
                               @RequestParam(name = "seasonFilter", required = false) List<String> seasonStrings,
                               Model model, Principal principal) {
        try {
            logger.info("priceSort: {}", priceSort);
            logger.info("nameSort: {}", nameSort);
            logger.info("nameSearch: {}", nameSearch);
            logger.info("producers: {}", producers);
            logger.info("priceMin: {}", priceMin);
            logger.info("priceMax: {}", priceMax);
            logger.info("volumes: {}", volumes);
            logger.info("sexStrings: {}", sexStrings);
            logger.info("aromaStrings: {}", aromaStrings);
            logger.info("saturationStrings: {}", saturationStrings);
            logger.info("dayTimeStrings: {}", dayTimeStrings);
            logger.info("specificityStrings: {}", specificityStrings);
            logger.info("seasonStrings: {}", seasonStrings);

            Iterable<Perfume> perfumes = perfumeService.getPerfumes(nameSearch);
            perfumes = perfumeService.filterPerfumes(perfumes, producers, priceMin, priceMax, volumes, sexStrings, aromaStrings, saturationStrings, dayTimeStrings, specificityStrings, seasonStrings);
            perfumes = perfumeService.listAllSortedByPrice(perfumes, priceSort);
            perfumes = perfumeService.listAllSortedByName(perfumes, nameSort);

            Iterable<String> allProducers = perfumeService.listAllProducers();
            Iterable<Integer> allVolumes = perfumeService.listAllVolumes();
            String[] allSexes = perfumeService.listAllSexes();
            String[] allAromas = perfumeService.listAllAromas();
            String[] allSeasons = perfumeService.listAllSeasons();
            String[] allSaturations = perfumeService.listAllSaturations();
            String[] allSpecificities = perfumeService.listAllSpecificities();
            String[] allDayTimes = perfumeService.listAllDayTimes();

            model.addAttribute("perfumes", perfumes);

            model.addAttribute("priceSort", priceSort);
            model.addAttribute("nameSort", nameSort);

            model.addAttribute("nameSearch", nameSearch);

            model.addAttribute("selectedProducers", producers != null ? producers : new ArrayList<>());
            model.addAttribute("priceMin", priceMin);
            model.addAttribute("priceMax", priceMax);
            model.addAttribute("selectedVolumes", volumes != null ? volumes : new ArrayList<>());
            model.addAttribute("selectedSexes", sexStrings != null ? sexStrings : new ArrayList<>());
            model.addAttribute("selectedAromas", aromaStrings != null ? aromaStrings : new ArrayList<>());
            model.addAttribute("selectedSaturations", saturationStrings != null ? saturationStrings : new ArrayList<>());
            model.addAttribute("selectedDayTimes", dayTimeStrings != null ? dayTimeStrings : new ArrayList<>());
            model.addAttribute("selectedSpecificities", specificityStrings != null ? specificityStrings : new ArrayList<>());
            model.addAttribute("selectedSeasons", seasonStrings != null ? seasonStrings : new ArrayList<>());

            model.addAttribute("user", perfumeService.getUserByPrincipal(principal));

            model.addAttribute("allProducers", allProducers);
            model.addAttribute("allVolumes", allVolumes);
            model.addAttribute("allSexes", allSexes);
            model.addAttribute("allAromas", allAromas);
            model.addAttribute("allSaturations", allSaturations);
            model.addAttribute("allSeasons", allSeasons);
            model.addAttribute("allDayTimes", allDayTimes);
            model.addAttribute("allSpecificities", allSpecificities);

            return "perfumes";
        } catch (Exception e) {
            logger.error("Error in perfumesMain method", e);
            throw e;
        }
    }

    /**
     * Handles GET requests to view a specific perfume's details.
     *
     * @param id    the ID of the perfume.
     * @param model the model to pass attributes to the view.
     * @return the name of the view to render.
     */
    @GetMapping("/perfumes/{id}")
    public String perfumeInfo(@PathVariable Long id, Model model, Principal principal) {
        Perfume perfume = perfumeService.getPerfumeById(id);
        model.addAttribute("perfume", perfume);
        List<String> imageUrls = perfume.getImages().stream()
                .map(image -> "/images/" + image.getId())
                .collect(Collectors.toList());
        model.addAttribute("imageUrls", imageUrls);
        model.addAttribute("user", perfumeService.getUserByPrincipal(principal));
        return "perfumeInfo";
    }

    /**
     * Handles POST requests to add a new perfume.
     *
     * @param file1   the first image file.
     * @param file2   the second image file.
     * @param file3   the third image file.
     * @param perfume the perfume details.
     * @return a redirect to the perfumes page.
     * @throws IOException if an error occurs while handling the image files.
     */
    @PostMapping("/perfumes/add")
    public String addPerfume(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                             @RequestParam("file3") MultipartFile file3, Perfume perfume) throws IOException {
        perfumeService.savePerfume(perfume, file1, file2, file3);
        return "redirect:/perfumes";
    }

    /**
     * Handles POST requests to delete a perfume.
     *
     * @param id the ID of the perfume to delete.
     * @return a redirect to the perfumes page.
     */
    @PostMapping("/perfumes/delete/{id}")
    public String deletePerfume(@PathVariable Long id) {
        perfumeService.deletePerfume(id);
        return "redirect:/perfumes";
    }

    @GetMapping("/perfumes/resetFilters")
    public String resetFilters(RedirectAttributes redirectAttributes) {
        // Видаляємо атрибути з параметрами фільтрів
        redirectAttributes.addAttribute("priceSort", null);
        redirectAttributes.addAttribute("nameSort", null);
        redirectAttributes.addAttribute("nameSearch", null);
        redirectAttributes.addAttribute("producerFilter", null);
        redirectAttributes.addAttribute("priceMinFilter", null);
        redirectAttributes.addAttribute("priceMaxFilter", null);
        redirectAttributes.addAttribute("volumeFilter", null);
        redirectAttributes.addAttribute("sexFilter", null);
        redirectAttributes.addAttribute("aromaFilter", null);
        redirectAttributes.addAttribute("saturationFilter", null);
        redirectAttributes.addAttribute("dayTimeFilter", null);
        redirectAttributes.addAttribute("specificityFilter", null);
        redirectAttributes.addAttribute("seasonFilter", null);

        // Перенаправляємо на головну сторінку з очищеними фільтрами
        return "redirect:/perfumes";
    }

    @GetMapping("/perfumes/edit/{id}")
    public String editPerfume(@PathVariable Long id, Model model, Principal principal) {
        Perfume perfume = perfumeService.getPerfumeById(id);
        model.addAttribute("perfume", perfume);
        model.addAttribute("user", perfumeService.getUserByPrincipal(principal));
        return "perfume-edit";
    }

    @PostMapping("/perfumes/update/{id}")
    public String updatePerfume(@RequestParam Long id, Perfume updatedPerfume,
                                @RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3) throws IOException {
        Perfume updated = perfumeService.updatePerfume(id, updatedPerfume, file1, file2, file3);
        if (updated != null) {
            return "redirect:/perfumes/" + id;
        }
        return "redirect:/perfumes";
    }

}
