package com.betrybe.agrix.controller.dto;

import com.betrybe.agrix.models.entities.Fertilizer;

/**
 * FertilizerDto.
 */
public record FertilizerDto(
    Integer id,
    String name,
    String brand,
    String composition) {  
  
  public Fertilizer toFertilizer() {
    return new Fertilizer(id, name, brand, composition, null);
  }
}
