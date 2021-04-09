package com.se.project.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarMapperTest {

    private lateinit var carMapper: CarMapper

    @BeforeEach
    fun setUp() {
        carMapper = CarMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(carMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(carMapper.fromId(null)).isNull()
    }
}
