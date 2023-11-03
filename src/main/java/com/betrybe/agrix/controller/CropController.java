package com.betrybe.agrix.controller;

import com.betrybe.agrix.controller.dto.CropDto;
import com.betrybe.agrix.controller.dto.FertilizerDto;
import com.betrybe.agrix.exception.CropNotFoundException;
import com.betrybe.agrix.exception.FertilizerNotFoundException;
import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.entities.Fertilizer;
import com.betrybe.agrix.service.CropService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CropController.
 */
@RestController
@RequestMapping("/crops")
public class CropController {

  @Autowired
  private CropService cropService;

  @GetMapping
  @Secured({"MANAGER", "ADMIN"})
  public ResponseEntity<List<CropDto>> getAllCrops() {
    List<CropDto> cropDtos = cropService.getAllCrops();
    return ResponseEntity.ok(cropDtos);
  }

  /**
   * Get Crop.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Object> getCropById(@PathVariable Integer id) {
    try {
      CropDto cropDto = cropService.getCropById(id);
      return ResponseEntity.ok(cropDto);
    } catch (CropNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plantação não encontrada!");
    }
  }

  /**
   * Create Crop.
   */
  @GetMapping("/search")
  public ResponseEntity<List<CropDto>> searchCropsByHarvestDate(
      @RequestParam("start") LocalDate startDate,
      @RequestParam("end") LocalDate endDate) {
    List<Crop> crops = cropService.searchCropsByHarvestDate(startDate, endDate);

    List<CropDto> cropDtos = crops.stream()
        .map(crop -> new CropDto(
            crop.getId(),
            crop.getName(),
            crop.getPlantedArea(),
            crop.getPlantedDate(),
            crop.getHarvestDate(),
            crop.getFarm().getId()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(cropDtos);
  }

  /**
   * Create Crop.
   */
  @PostMapping("/{cropId}/fertilizers/{fertilizerId}")
  public ResponseEntity<String> associateCropWithFertilizer(
      @PathVariable Integer cropId,
      @PathVariable Integer fertilizerId) {
    try {
      cropService.associateCropWithFertilizer(cropId, fertilizerId);
      return ResponseEntity.status(HttpStatus.CREATED).body(
          "Fertilizante e plantação associados com sucesso!");
    } catch (CropNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plantação não encontrada!");
    } catch (FertilizerNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fertilizante não encontrado!");
    }
  }

  /**
   * Get Fertilizers by Crop Id.
   */
  @GetMapping("/{cropId}/fertilizers")
  public ResponseEntity<Object> getFertilizersByCropId(@PathVariable Integer cropId) {
    try {
      List<Fertilizer> fertilizers = cropService.getFertilizersByCropId(cropId);

      List<FertilizerDto> fertilizerDtos = fertilizers.stream()
          .map(fertilizer -> new FertilizerDto(
              fertilizer.getId(),
              fertilizer.getName(),
              fertilizer.getBrand(),
              fertilizer.getComposition()))
          .collect(Collectors.toList());

      return ResponseEntity.ok(fertilizerDtos);
    } catch (CropNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plantação não encontrada!");
    }
  }
}

