package com.se.project.service
import com.se.project.domain.Car
import com.se.project.repository.CarRepository
import com.se.project.service.dto.CarDTO
import com.se.project.service.mapper.CarMapper
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Car].
 */
@Service
@Transactional
class CarService(
    private val carRepository: CarRepository,
    private val carMapper: CarMapper
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(carDTO: CarDTO): CarDTO {
        log.debug("Request to save Car : $carDTO")

        var car = carMapper.toEntity(carDTO)
        car = carRepository.save(car)
        return carMapper.toDto(car)
    }

    /**
     * Get all the cars.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(): MutableList<CarDTO> {
        log.debug("Request to get all Cars")
        return carRepository.findAll()
            .mapTo(mutableListOf(), carMapper::toDto)
    }

    /**
     * Get one car by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<CarDTO> {
        log.debug("Request to get Car : $id")
        return carRepository.findById(id)
            .map(carMapper::toDto)
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete Car : $id")

        carRepository.deleteById(id)
    }
}
