package me.yaman.can.demo.model

import java.time.Instant
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class SimpleModel(
    val id: Long? = null,
    @field:NotBlank(message = "Name is mandatory")
    val name: String,
    @field:Min(0)
    val counter: Long,
    val createDate: Instant = Instant.now(),
    val updateDate: Instant? = null
)
