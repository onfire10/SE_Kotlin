package com.se.project.service.mapper

import com.se.project.domain.Car
import com.se.project.service.dto.CarDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [Car] and its DTO [CarDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface CarMapper :
    EntityMapper<CarDTO, Car> {

    override fun toEntity(carDTO: CarDTO): Car

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val car = Car()
        car.id = id
        car
    }
}
