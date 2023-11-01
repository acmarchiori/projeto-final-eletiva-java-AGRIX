package com.betrybe.agrix.service;

import com.betrybe.agrix.controller.dto.CropDto;
import com.betrybe.agrix.exception.CropNotFoundException;
import com.betrybe.agrix.exception.FarmNotFoundException;
import com.betrybe.agrix.exception.FertilizerNotFoundException;
import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.entities.Farm;
import com.betrybe.agrix.models.entities.Fertilizer;
import com.betrybe.agrix.models.repositories.CropRepository;
import com.betrybe.agrix.models.repositories.FarmRepository;
import com.betrybe.agrix.models.repositories.FertilizerRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CropService.
 */
@Service
public class CropService {

  @Autowired
  private FarmRepository farmRepository;

  @Autowired
  private CropRepository cropRepository;

  @Autowired
  private FarmService farmService;

  @Autowired
  private FertilizerRepository fertilizerRepository;

  /**
   * Create Crop.
   */
  public CropDto createCrop(Integer farmId, CropDto newCropDto) {
    Optional<Farm> farm = farmService.getFarmById(farmId);

    if (farm.isPresent()) {
      Crop newCrop = new Crop();
      newCrop.setName(newCropDto.name());
      newCrop.setPlantedArea(newCropDto.plantedArea());
      newCrop.setFarm(farm.get());
      newCrop.setPlantedDate(newCropDto.plantedDate());
      newCrop.setHarvestDate(newCropDto.harvestDate());

      Crop createdCrop = cropRepository.save(newCrop);
      return CropDto.toCrop(createdCrop);
    } else {
      throw new FarmNotFoundException();
    }
  }


  /**
   * Get Crops by Farm Id.
   */
  public List<CropDto> getCropsByFarmId(Integer farmId) {
    Optional<Farm> farm = farmService.getFarmById(farmId);

    if (farm.isPresent()) {
      List<Crop> crops = cropRepository.findCropsByFarmId(farmId);

      return crops.stream()
          .map(CropDto::toCrop)
          .collect(Collectors.toList());
    } else {
      throw new FarmNotFoundException();
    }
  }

  /**
   * Get all Crops.
   */
  public List<CropDto> getAllCrops() {
    List<Crop> crops = cropRepository.findAll();

    return crops.stream()
        .map(CropDto::toCrop)
        .collect(Collectors.toList());
  }

  /**
   * Get Crop by id.
   */
  public CropDto getCropById(Integer id) {
    Crop crop = cropRepository.findById(id)
        .orElseThrow(CropNotFoundException::new);
    return CropDto.toCrop(crop);
  }

  /**
   * Search Crops by Harvest Date.
   */
  public List<Crop> searchCropsByHarvestDate(LocalDate startDate, LocalDate endDate) {
    // Busca as plantações com datas de colheita dentro do intervalo inclusivo
    List<Crop> crops = cropRepository.findCropsByHarvestDateBetween(startDate, endDate);

    return crops;
  }

  /**
   * Associate Crop with Fertilizer.
   */
  public void associateCropWithFertilizer(Integer cropId, Integer fertilizerId) {
    Crop crop = cropRepository.findById(cropId)
        .orElseThrow(CropNotFoundException::new);

    Fertilizer fertilizer = fertilizerRepository.findById(fertilizerId)
        .orElseThrow(FertilizerNotFoundException::new);

    crop.getFertilizers().add(fertilizer);
    cropRepository.save(crop);
  }

  /**
   * Get Fertilizers by Crop Id.
   */
  public List<Fertilizer> getFertilizersByCropId(Integer cropId) {
    Crop crop = cropRepository.findById(cropId)
        .orElseThrow(CropNotFoundException::new);

    return crop.getFertilizers();
  }
}

