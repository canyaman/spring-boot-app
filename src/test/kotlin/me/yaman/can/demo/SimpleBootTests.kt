package me.yaman.can.demo

import me.yaman.can.demo.model.SimpleModel
import me.yaman.can.demo.support.DockerComposeHelper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.h2.console.enabled=false",
        "database.vendor=h2"
    ]
)
@ActiveProfiles("r2dbc", "h2-embedded", "test-migrate", "public")
class SimpleBootEmbeddedTests(): SimpleBootTests() {
}

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.h2.console.enabled=false",
        "database.vendor=postgresql"
    ]
)
@ActiveProfiles("r2dbc", "postgresql", "migrate", "public")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleBootDockerComposeTests(): SimpleBootTests() {
    // companion object {
    //     @RegisterExtension
    //     @JvmField
    //     val dockerCompose: DockerComposeHelper = DockerComposeHelper("postgres")
    // }
}

abstract class SimpleBootTests() {
    private val log = LoggerFactory.getLogger(javaClass)
    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun readSimpleModelTest() {
        val response = webClient.get().uri("/simple/12")
            .exchange()
            .expectStatus().isOk.expectBody(Map::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.get("id")).isEqualTo(12)
    }

    @Test
    fun createSimpleModelTest() {
        val response = webClient.post().uri("/simple/")
            .bodyValue(SimpleModel(10, "Simple Create Model", 2L))
            .exchange()
            .expectStatus().isOk.expectBody(Map::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.get("id")).isEqualTo(10)
    }

    @Test
    fun createInvalidSimpleModelTest() {
        val response = webClient.post().uri("/simple/")
            .bodyValue(SimpleModel(10L, "", 2L))
            .exchange()
            .expectStatus().isBadRequest.expectBody(Map::class.java).returnResult()
        log.info("Invalid simple model response {}", response.responseBody)
        Assertions.assertThat(response.responseBody?.get("error")).isNotNull
        Assertions.assertThat(response.responseBody?.get("message").toString().contains("Name is mandatory")).isTrue
    }

    @Test
    fun updateSimpleModelTest() {
        val response = webClient.put().uri("/simple/20")
            .bodyValue(SimpleModel(20, "Simple Create Model", 2))
            .exchange()
            .expectStatus().isOk.expectBody(Map::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.get("id")).isEqualTo(20)
    }

    @Test
    fun updateInvalidSimpleModelTest() {
        val response = webClient.put().uri("/simple/20")
            .bodyValue(SimpleModel(20, "Simple Create Model", -1))
            .exchange()
            .expectStatus().isBadRequest.expectBody(Map::class.java).returnResult()
        log.info("Invalid simple model response {}", response.responseBody)
        Assertions.assertThat(response.responseBody?.get("error")).isNotNull
    }

    @Test
    fun getPageCountTest() {
        val response = webClient.get().uri("/simple/count")
            .exchange()
            .expectStatus().isOk.expectBody(Map::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.get("totalElements")).isEqualTo(35)
    }

    @Test
    fun getFirstPageTest() {
        val response = webClient.get().uri("/simple/list?size=10&page=0")
            .exchange()
            .expectStatus().isOk.expectBody(List::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.size).isEqualTo(10)
    }
}
