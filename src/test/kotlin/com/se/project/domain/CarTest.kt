package com.se.project.domain

import com.se.project.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Car::class)
        val car1 = Car()
        car1.id = 1L
        val car2 = Car()
        car2.id = car1.id
        assertThat(car1).isEqualTo(car2)
        car2.id = 2L
        assertThat(car1).isNotEqualTo(car2)
        car1.id = null
        assertThat(car1).isNotEqualTo(car2)
    }
}
