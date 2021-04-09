package com.se.project.web.rest

import com.se.project.SeKotlinApp
import com.se.project.domain.Car
import com.se.project.repository.CarRepository
import com.se.project.service.CarService
import com.se.project.service.mapper.CarMapper
import com.se.project.web.rest.errors.ExceptionTranslator
import java.math.BigDecimal
import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator

/**
 * Integration tests for the [CarResource] REST controller.
 *
 * @see CarResource
 */
@SpringBootTest(classes = [SeKotlinApp::class])
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    @Autowired
    private lateinit var carRepository: CarRepository

    @Autowired
    private lateinit var carMapper: CarMapper

    @Autowired
    private lateinit var carService: CarService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    private lateinit var restCarMockMvc: MockMvc

    private lateinit var car: Car

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val carResource = CarResource(carService)
         this.restCarMockMvc = MockMvcBuilders.standaloneSetup(carResource)
             .setCustomArgumentResolvers(pageableArgumentResolver)
             .setControllerAdvice(exceptionTranslator)
             .setConversionService(createFormattingConversionService())
             .setMessageConverters(jacksonMessageConverter)
             .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        car = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createCar() {
        val databaseSizeBeforeCreate = carRepository.findAll().size

        // Create the Car
        val carDTO = carMapper.toDto(car)
        restCarMockMvc.perform(
            post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(carDTO))
        ).andExpect(status().isCreated)

        // Validate the Car in the database
        val carList = carRepository.findAll()
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1)
        val testCar = carList[carList.size - 1]
        assertThat(testCar.carType).isEqualTo(DEFAULT_CAR_TYPE)
        assertThat(testCar.brand).isEqualTo(DEFAULT_BRAND)
        assertThat(testCar.kwPower).isEqualTo(DEFAULT_KW_POWER)
        assertThat(testCar.usdPrice).isEqualTo(DEFAULT_USD_PRICE)
        assertThat(testCar.isRented).isEqualTo(DEFAULT_IS_RENTED)
    }

    @Test
    @Transactional
    fun createCarWithExistingId() {
        val databaseSizeBeforeCreate = carRepository.findAll().size

        // Create the Car with an existing ID
        car.id = 1L
        val carDTO = carMapper.toDto(car)

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc.perform(
            post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(carDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Car in the database
        val carList = carRepository.findAll()
        assertThat(carList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCars() {
        // Initialize the database
        carRepository.saveAndFlush(car)

        // Get all the carList
        restCarMockMvc.perform(get("/api/cars?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.id?.toInt())))
            .andExpect(jsonPath("$.[*].carType").value(hasItem(DEFAULT_CAR_TYPE)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].kwPower").value(hasItem(DEFAULT_KW_POWER)))
            .andExpect(jsonPath("$.[*].usdPrice").value(hasItem(DEFAULT_USD_PRICE?.toInt())))
            .andExpect(jsonPath("$.[*].isRented").value(hasItem(DEFAULT_IS_RENTED))) }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getCar() {
        // Initialize the database
        carRepository.saveAndFlush(car)

        val id = car.id
        assertNotNull(id)

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.id?.toInt()))
            .andExpect(jsonPath("$.carType").value(DEFAULT_CAR_TYPE))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.kwPower").value(DEFAULT_KW_POWER))
            .andExpect(jsonPath("$.usdPrice").value(DEFAULT_USD_PRICE?.toInt()))
            .andExpect(jsonPath("$.isRented").value(DEFAULT_IS_RENTED)) }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingCar() {
        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateCar() {
        // Initialize the database
        carRepository.saveAndFlush(car)

        val databaseSizeBeforeUpdate = carRepository.findAll().size

        // Update the car
        val id = car.id
        assertNotNull(id)
        val updatedCar = carRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar)
        updatedCar.carType = UPDATED_CAR_TYPE
        updatedCar.brand = UPDATED_BRAND
        updatedCar.kwPower = UPDATED_KW_POWER
        updatedCar.usdPrice = UPDATED_USD_PRICE
        updatedCar.isRented = UPDATED_IS_RENTED
        val carDTO = carMapper.toDto(updatedCar)

        restCarMockMvc.perform(
            put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(carDTO))
        ).andExpect(status().isOk)

        // Validate the Car in the database
        val carList = carRepository.findAll()
        assertThat(carList).hasSize(databaseSizeBeforeUpdate)
        val testCar = carList[carList.size - 1]
        assertThat(testCar.carType).isEqualTo(UPDATED_CAR_TYPE)
        assertThat(testCar.brand).isEqualTo(UPDATED_BRAND)
        assertThat(testCar.kwPower).isEqualTo(UPDATED_KW_POWER)
        assertThat(testCar.usdPrice).isEqualTo(UPDATED_USD_PRICE)
        assertThat(testCar.isRented).isEqualTo(UPDATED_IS_RENTED)
    }

    @Test
    @Transactional
    fun updateNonExistingCar() {
        val databaseSizeBeforeUpdate = carRepository.findAll().size

        // Create the Car
        val carDTO = carMapper.toDto(car)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc.perform(
            put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(carDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Car in the database
        val carList = carRepository.findAll()
        assertThat(carList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteCar() {
        // Initialize the database
        carRepository.saveAndFlush(car)

        val databaseSizeBeforeDelete = carRepository.findAll().size

        // Delete the car
        restCarMockMvc.perform(
            delete("/api/cars/{id}", car.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val carList = carRepository.findAll()
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_CAR_TYPE = "AAAAAAAAAA"
        private const val UPDATED_CAR_TYPE = "BBBBBBBBBB"

        private const val DEFAULT_BRAND = "AAAAAAAAAA"
        private const val UPDATED_BRAND = "BBBBBBBBBB"

        private const val DEFAULT_KW_POWER: Int = 1
        private const val UPDATED_KW_POWER: Int = 2

        private val DEFAULT_USD_PRICE: BigDecimal = BigDecimal(1)
        private val UPDATED_USD_PRICE: BigDecimal = BigDecimal(2)

        private const val DEFAULT_IS_RENTED: Boolean = false
        private const val UPDATED_IS_RENTED: Boolean = true

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Car {
            val car = Car(
                carType = DEFAULT_CAR_TYPE,
                brand = DEFAULT_BRAND,
                kwPower = DEFAULT_KW_POWER,
                usdPrice = DEFAULT_USD_PRICE,
                isRented = DEFAULT_IS_RENTED
            )

            return car
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Car {
            val car = Car(
                carType = UPDATED_CAR_TYPE,
                brand = UPDATED_BRAND,
                kwPower = UPDATED_KW_POWER,
                usdPrice = UPDATED_USD_PRICE,
                isRented = UPDATED_IS_RENTED
            )

            return car
        }
    }
}
