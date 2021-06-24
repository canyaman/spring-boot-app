package me.yaman.can.demo

import me.yaman.can.demo.config.DatabaseConfig
import me.yaman.can.demo.entity.Simple
import me.yaman.can.demo.repository.SimpleRepository
import name.nkonev.r2dbc.migrate.autoconfigure.R2dbcMigrateAutoConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier
import java.time.Instant

@DataR2dbcTest(
    properties = [
        "r2dbc.migrate.enable=false"
    ]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("r2dbc", "postgresql-tc")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ImportAutoConfiguration(R2dbcMigrateAutoConfiguration::class)
@Import(DatabaseConfig::class)
@Tag("testcontainers")
class SimpleDataContainerTests : SimpleDataTests()

@DataR2dbcTest(
    properties = [
        "database.vendor=h2"
    ]
)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("r2dbc", "h2-embedded", "test-migrate")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ImportAutoConfiguration(R2dbcMigrateAutoConfiguration::class)
@Tag("embedded")
class SimpleDataEmbeddedTests : SimpleDataTests()

abstract class SimpleDataTests {
    @Autowired
    lateinit var databaseClient: DatabaseClient

    @Autowired
    lateinit var simpleModels: SimpleRepository

    @Test
    fun testEmptySimpleTable() {
        databaseClient.sql("SELECT * FROM SIMPLE").fetch().all().count().`as`(StepVerifier::create).assertNext { rowCount ->
            Assertions.assertThat(rowCount).isGreaterThanOrEqualTo(0)
        }.verifyComplete()
        simpleModels.findAll().count().`as`(StepVerifier::create).assertNext { rowCount ->
            Assertions.assertThat(rowCount).isGreaterThanOrEqualTo(0)
        }.verifyComplete()
    }

    @Test
    fun testSingleSimpleTable() {
        val singleSimpleItem = Simple(null, "Single Simple", 0, Instant.now(), null)
        simpleModels.save(singleSimpleItem).`as`(StepVerifier::create).assertNext {
            Assertions.assertThat(it.id).isNotNull
        }.verifyComplete()
        databaseClient.sql("SELECT * FROM SIMPLE").fetch().all().count().`as`(StepVerifier::create).assertNext { rowCount ->
            Assertions.assertThat(rowCount).isGreaterThanOrEqualTo(1)
        }.verifyComplete()
        simpleModels.findAll().count().`as`(StepVerifier::create).assertNext { rowCount ->
            Assertions.assertThat(rowCount).isGreaterThanOrEqualTo(1)
        }.verifyComplete()
    }
}
