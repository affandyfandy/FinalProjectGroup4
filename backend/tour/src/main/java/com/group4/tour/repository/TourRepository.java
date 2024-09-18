package com.group4.tour.repository;

import com.group4.tour.model.Tour;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, String> {
    List<Tour> findByTitleContaining(String title);

    List<Tour> findByPricesBetween(int minPrice, int maxPrice, Sort sort);

    List<Tour> findByLocationContaining(String location);
}
