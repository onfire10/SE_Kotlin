package com.se.project.service.dto

import com.se.project.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(CarDTO::class)
        val carDTO1 = CarDTO()
        carDTO1.id = 1L
        val carDTO2 = CarDTO()
        assertThat(carDTO1).isNotEqualTo(carDTO2)
        carDTO2.id = carDTO1.id
        assertThat(carDTO1).isEqualTo(carDTO2)
        carDTO2.id = 2L
        assertThat(carDTO1).isNotEqualTo(carDTO2)
        carDTO1.id = null
        assertThat(carDTO1).isNotEqualTo(carDTO2)
    }
}
