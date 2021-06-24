package me.yaman.can.demo.repository

import me.yaman.can.demo.entity.Simple
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SimpleRepository : ReactiveSortingRepository<Simple, Long>
