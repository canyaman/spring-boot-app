package me.yaman.can.demo

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import me.yaman.can.demo.controller.TimeController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
    ]
)
@ActiveProfiles("r2dbc", "postgresql-test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTests(
    @Autowired val webClient: WebTestClient
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun contextLoads() {
        log.info("Spring context is loaded.")
        Assertions.assertThat(webClient).isNotNull
    }

    @Test
    fun timeControllerNowTest() {
        val response = webClient.get().uri("/time/now")
            .exchange()
            .expectStatus().isOk.expectBody(TimeController.ServerTime::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.local).isBefore(LocalDateTime.now())
    }

    @Test
    fun exceptionTest() {
        val response = webClient.get().uri("/resource/not-exist")
            .exchange()
            .expectStatus().isNotFound.expectBody(JsonNode::class.java).returnResult()
        val service = response.responseBody?.get("service") as? TextNode
        Assertions.assertThat(service?.textValue()).isNotEmpty
    }
}
