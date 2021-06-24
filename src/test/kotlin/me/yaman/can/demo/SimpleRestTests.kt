package me.yaman.can.demo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.yaman.can.demo.controller.SimpleController
import me.yaman.can.demo.model.SimpleModel
import me.yaman.can.demo.repository.SimpleRepository
import me.yaman.can.demo.service.SimpleService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono


@WebFluxTest(controllers = [SimpleController::class], excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class])
@ActiveProfiles("web-test")
@Import(SimpleService::class)
class SimpleRestTests(
    @Autowired val webClient: WebTestClient
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @MockkBean
    lateinit var simpleRepository: SimpleRepository

    @Test
    fun readSimpleModelTest() {
        every { simpleRepository.findById(12) } returns Fake.simpleEntity(12).toMono()
        val response = webClient.get().uri("/simple/12")
            .exchange()
            .expectStatus().isOk.expectBody(Map::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.get("id")).isEqualTo(12)
    }

    @Test
    fun createSimpleModelTest() {
        val response = webClient.post().uri("/simple/")
            .bodyValue(SimpleModel(10, "Simple Create Model", 2))
            .exchange()
            .expectStatus().isOk.expectBody(Map::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.get("id")).isEqualTo(10)
    }

    @Test
    fun createInvalidSimpleModelTest() {
        every { simpleRepository.save(any()) } returns Fake.simpleEntity(10).toMono()
        val response = webClient.post().uri("/simple/")
            .bodyValue(SimpleModel(null, "", 1))
            .exchange()
            .expectStatus().isBadRequest.expectBody(Map::class.java).returnResult()
        log.info("Invalid simple model response {}", response.responseBody)
        Assertions.assertThat(response.responseBody?.get("error")).isNotNull
        Assertions.assertThat(response.responseBody?.get("message").toString().contains("Name is mandatory")).isTrue
    }

    @Test
    fun updateSimpleModelTest() {
        every { simpleRepository.findById(20) } returns Fake.simpleEntity(20).toMono()
        every { simpleRepository.save(any()) } returns Fake.simpleEntity(20).toMono()
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
        every { simpleRepository.count() } returns 35L.toMono()
        val response = webClient.get().uri("/simple/count")
            .exchange()
            .expectStatus().isOk.expectBody(Map::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.get("totalElements")).isEqualTo(35)
    }

    @Test
    fun getFirstPageTest() {
        every { simpleRepository.findAll(any()) } returns Fake.listOfSimple(10).toFlux()
        val response = webClient.get().uri("/simple/list?size=10&page=0")
            .exchange()
            .expectStatus().isOk.expectBody(List::class.java).returnResult()
        Assertions.assertThat(response.responseBody?.size).isEqualTo(10)
    }
}
