package com.se.project.web.rest

import com.se.project.service.CarService
import com.se.project.service.dto.CarDTO
import com.se.project.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val ENTITY_NAME = "car"
/**
 * REST controller for managing [com.se.project.domain.Car].
 */
@RestController
@RequestMapping("/api")
class CarResource(
    private val carService: CarService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /cars` : Create a new car.
     *
     * @param carDTO the carDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new carDTO, or with status `400 (Bad Request)` if the car has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cars")
    fun createCar(@RequestBody carDTO: CarDTO): ResponseEntity<CarDTO> {
        log.debug("REST request to save Car : $carDTO")
        if (carDTO.id != null) {
            throw BadRequestAlertException(
                "A new car cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = carService.save(carDTO)
        return ResponseEntity.created(URI("/api/cars/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /cars` : Updates an existing car.
     *
     * @param carDTO the carDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated carDTO,
     * or with status `400 (Bad Request)` if the carDTO is not valid,
     * or with status `500 (Internal Server Error)` if the carDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cars")
    fun updateCar(@RequestBody carDTO: CarDTO): ResponseEntity<CarDTO> {
        log.debug("REST request to update Car : $carDTO")
        if (carDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = carService.save(carDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, false, ENTITY_NAME,
                     carDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /cars` : get all the cars.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of cars in body.
     */
    @GetMapping("/cars")
    fun getAllCars(): MutableList<CarDTO> {
        log.debug("REST request to get all Cars")

        return carService.findAll()
            }

    /**
     * `GET  /cars/:id` : get the "id" car.
     *
     * @param id the id of the carDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the carDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/cars/{id}")
    fun getCar(@PathVariable id: Long): ResponseEntity<CarDTO> {
        log.debug("REST request to get Car : $id")
        val carDTO = carService.findOne(id)
        return ResponseUtil.wrapOrNotFound(carDTO)
    }
    /**
     *  `DELETE  /cars/:id` : delete the "id" car.
     *
     * @param id the id of the carDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/cars/{id}")
    fun deleteCar(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Car : $id")

        carService.delete(id)
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build()
    }
}
