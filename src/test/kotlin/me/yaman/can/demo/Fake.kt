package me.yaman.can.demo

import com.github.javafaker.Faker
import me.yaman.can.demo.model.SimpleModel
import me.yaman.can.demo.service.toEntity
import java.time.Instant

object Fake {
    fun simpleModel(id:Long) = SimpleModel(id, Faker.instance().name().name(),Faker.instance().number().numberBetween(0,1000).toLong(), Instant.now(),null)
    fun simpleEntity(id:Long) = simpleModel(12).toEntity()
    fun listOfSimple(count: Long) = arrayOf(
        simpleEntity(1),
        simpleEntity(2),
        simpleEntity(3),
        simpleEntity(4),
        simpleEntity(5),
        simpleEntity(6),
        simpleEntity(7),
        simpleEntity(8),
        simpleEntity(9),
        simpleEntity(10))
}