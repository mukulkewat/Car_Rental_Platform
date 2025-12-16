package com.crp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crp.exception.BusinessException;
import com.crp.exception.ResourceNotFoundException;
import com.crp.mapper.CarMapper;
import com.crp.model.Car;
import com.crp.repository.CarRepo;
import com.crp.requestdto.CarRequestDTO;
import com.crp.responsedto.CarResponseDTO;

@Service
public class CarService {
	@Autowired
    private final CarRepo carRepo;
	@Autowired private CarMapper carMapper;

    public CarService(CarRepo carRepo, CarMapper carMapper) {
        this.carRepo = carRepo;
        this.carMapper = carMapper;
    }

    // 1️ List all cars
    public List<CarResponseDTO> listAllCars() {
        return carRepo.findAll()
                .stream()
                .map(carMapper::toResponse)
                .toList();
    }


    // 2️ Find car by ID
    public CarResponseDTO findCarById(Long id) {

        Car car = carRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Car not found with id: " + id));

        return carMapper.toResponse(car);
    }

    // 3️ Add new car (ADMIN)
    public CarResponseDTO addCar(CarRequestDTO carDto) {

        if (carDto == null) {
            throw new BusinessException("Car details must not be null");
        }
        Car car = carMapper.toEntity(carDto);
        carRepo.save(car);
      return carMapper.toResponse(car);
    }

    // 4️ Delete car (ADMIN)
    public void deleteCar(Long id) {

        if (!carRepo.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Car not found with id: " + id);
        }

        carRepo.deleteById(id);
    }

    // 5️ Find available cars between dates
    public List<CarResponseDTO> findAvailableCars(LocalDate start, LocalDate end) {

        if (start == null || end == null) {
            throw new BusinessException("Start date and end date must be provided");
        }

        if (end.isBefore(start)) {
            throw new BusinessException("End date cannot be before start date");
        }
        
        return carRepo.findAvailableCars(start, end)
        .stream()
        .map(carMapper::toResponse)
        .toList();
    }

    // 6️ Update availability (internal use)
    public CarResponseDTO updateAvailability(Long carId, boolean available) {

        Car car = carRepo.findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Car not found with id: " + carId));
        car.setAvailable(available);
        carRepo.save(car);
        return carMapper.toResponse(car);
    }
}
