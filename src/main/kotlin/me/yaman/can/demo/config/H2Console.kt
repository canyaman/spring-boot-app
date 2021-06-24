package me.yaman.can.demo.config

import org.h2.tools.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("h2-console")
class H2Console {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    private var webServer: Server? = null

    @Value("\${spring.h2.console.port}")
    var h2ConsolePort: Int? = null

    @Value("\${spring.h2.console.enabled}")
    var h2Enabled: Boolean? = null

    @EventListener(ContextRefreshedEvent::class)
    fun start() {
        if (h2Enabled != true) { return }
        if (webServer == null) {
            log.info("H2 console started on port $h2ConsolePort")
            webServer = Server.createWebServer("-web", "-webPort", h2ConsolePort.toString()).start()
        } else {
            log.info("H2 console is already running on port $h2ConsolePort")
        }
    }

    @EventListener(ContextClosedEvent::class)
    fun stop() {
        if (h2Enabled != true) { return }
        log.info("H2 console stopped on port $h2ConsolePort")
        webServer?.stop()
        webServer = null
    }
}
