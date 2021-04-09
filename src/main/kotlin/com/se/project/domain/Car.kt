package com.se.project.domain

import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "car_type")
    var carType: String? = null,

    @Column(name = "brand")
    var brand: String? = null,

    @Column(name = "kw_power")
    var kwPower: Int? = null,

    @Column(name = "usd_price", precision = 21, scale = 2)
    var usdPrice: BigDecimal? = null,

    @Column(name = "is_rented")
    var isRented: Boolean? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Car) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Car{" +
        "id=$id" +
        ", carType='$carType'" +
        ", brand='$brand'" +
        ", kwPower=$kwPower" +
        ", usdPrice=$usdPrice" +
        ", isRented='$isRented'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
