package com.betrybe.agrix.models.repositories;

import com.betrybe.agrix.models.entities.Crop;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CropRepository.
 */
@Repository
public interface CropRepository extends JpaRepository<Crop, Integer> {
  List<Crop> findCropsByFarmId(Integer farmId);

  List<Crop> findCropsByHarvestDateBetween(LocalDate startDate, LocalDate endDate);
}
