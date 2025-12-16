package com.crp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crp.model.Car;
import com.crp.requestdto.CarRequestDTO;
import com.crp.responsedto.CarResponseDTO;

@Mapper(componentModel = "spring")
public interface CarMapper {

    // 🔹 Request DTO → Entity
    @Mapping(target = "id", ignore = true) // DB generated
    Car toEntity(CarRequestDTO dto);

    // 🔹 Entity → Response DTO
    CarResponseDTO toResponse(Car car);
}
