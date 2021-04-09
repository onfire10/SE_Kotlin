package com.se.project.service.dto

import java.io.Serializable
import java.math.BigDecimal

/**
 * A DTO for the [com.se.project.domain.Car] entity.
 */
data class CarDTO(

    var id: Long? = null,

    var carType: String? = null,

    var brand: String? = null,

    var kwPower: Int? = null,

    var usdPrice: BigDecimal? = null,

    var isRented: Boolean? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CarDTO) return false
        return id != null && id == other.id
    }

    override fun hashCode() = 31
}
