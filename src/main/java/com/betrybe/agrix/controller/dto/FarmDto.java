package com.betrybe.agrix.controller.dto;

import com.betrybe.agrix.models.entities.Farm;

/**
 * FarmDto.
 */
public record FarmDto(Integer id, String name, Double size) {

  public Farm toFarm() {
    return new Farm(id, name, size, null);
  }
}
