package com.example.Otto.repository;

import com.example.Otto.models.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The PerfumeRepository interface provides access to the database for Perfume entities.
 * It extends the JpaRepository interface, which provides CRUD operations for the Perfume entity.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 11.06.2024
 */
@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {

    /**
     * Retrieves a list of perfumes by their name.
     *
     * @param name the name or part of the name of the perfume to search for
     * @return a list of perfumes with the specified name or part of the name
     */
    List<Perfume> findByNameContaining(String name);

    @Query("SELECT DISTINCT p.producer FROM Perfume p")
    List<String> findAllProducers();

    @Query("SELECT DISTINCT p.volume FROM Perfume p")
    Iterable<Integer> findAllVolumes();
}
