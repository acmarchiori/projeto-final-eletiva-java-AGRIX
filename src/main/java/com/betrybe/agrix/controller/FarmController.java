package com.betrybe.agrix.controller;

import com.betrybe.agrix.controller.dto.CropDto;
import com.betrybe.agrix.controller.dto.FarmDto;
import com.betrybe.agrix.exception.FarmNotFoundException;
import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.entities.Farm;
import com.betrybe.agrix.service.CropService;
import com.betrybe.agrix.service.FarmService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FarmController.
 */
@RestController
@RequestMapping("/farms")
public class FarmController {

  private final FarmService farmService;

  @Autowired
  public FarmController(FarmService farmService) {
    this.farmService = farmService;
  }

  @Autowired
  private CropService cropService;

  @PostMapping()
  public ResponseEntity<Farm> createFarm(@RequestBody FarmDto farmDto) {
    Farm newFarm = farmService.insertFarm(farmDto.toFarm());
    return ResponseEntity.status(HttpStatus.CREATED).body(newFarm);
  }

  /**
   * GetMapping.
   */
  @GetMapping()
  public List<FarmDto> getAllFarms() {
    List<Farm> allFarms = farmService.getAllFarms();
    return allFarms.stream()
        .map((farm -> new FarmDto(farm.getId(), farm.getName(), farm.getSize()))).toList();
  }

  /**
   * GetMapping.
   */
  @GetMapping("/{farmId}")
  public ResponseEntity<Object> getFarmById(@PathVariable Integer farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);

    if (optionalFarm.isPresent()) {
      Farm farm = optionalFarm.get();
      return ResponseEntity.ok(farm);
    } else {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Fazenda não encontrada!");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
  }

  /**
   * PostMapping.
   */
  @PostMapping("/{farmId}/crops")
  public ResponseEntity<Object> createCrop(
      @PathVariable Integer farmId,
      @RequestBody CropDto cropDto) {
    CropDto createdCrop;

    try {
      // Crie um objeto Crop a partir dos dados do CropDto, incluindo as datas
      Crop newCrop = new Crop();
      newCrop.setName(cropDto.name());
      newCrop.setPlantedArea(cropDto.plantedArea());
      newCrop.setPlantedDate(cropDto.plantedDate());
      newCrop.setHarvestDate(cropDto.harvestDate());

      createdCrop = cropService.createCrop(farmId, cropDto);

      return ResponseEntity.status(HttpStatus.CREATED).body(createdCrop);
    } catch (FarmNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fazenda não encontrada!");
    }
  }


  /**
   * GetMapping.
   */
  @GetMapping("/{farmId}/crops")
  public ResponseEntity<Object> getCropsByFarmId(@PathVariable Integer farmId) {
    try {
      List<CropDto> cropDtos = cropService.getCropsByFarmId(farmId);
      return ResponseEntity.ok(cropDtos);
    } catch (FarmNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Collections.singletonMap("message", "Fazenda não encontrada!"));
    }
  }
}
