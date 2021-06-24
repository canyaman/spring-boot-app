package me.yaman.can.demo.controller

import io.swagger.v3.oas.annotations.Parameter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@RestController
@RequestMapping("time")
class TimeController {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    companion object {
        const val path = "/time/now"
    }

    @GetMapping("/now")
    fun now(
        @Parameter(description = "HTTP request sender Epoch Time in seconds")
        @RequestParam(name = "epoch") clientEpoch: Long? = null
    ): ServerTime {
        val local = LocalDateTime.now()
        val zoneId = ZoneId.systemDefault()
        val zoneOffset = zoneId.rules.getOffset(local)
        val instant = local.toInstant(zoneOffset)
        val serverEpoch = instant.epochSecond
        val diff: Long? = clientEpoch?.let { serverEpoch - it }
        return ServerTime(instant, serverEpoch, local, zoneId, zoneOffset, diff)
    }

    data class ServerTime(
        val utc: Instant,
        val epoch: Long,
        val local: LocalDateTime,
        val zone: ZoneId,
        val offset: ZoneOffset,
        val diff: Long?
    )
}
