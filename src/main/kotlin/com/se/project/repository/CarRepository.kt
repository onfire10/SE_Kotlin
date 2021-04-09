package com.se.project.repository

import com.se.project.domain.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Car] entity.
 */
@Suppress("unused")
@Repository
interface CarRepository : JpaRepository<Car, Long>
