package me.yaman.can.demo.support

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

class DockerComposeHelper(vararg services : String) : BeforeAllCallback, AfterAllCallback {

    private val environment : DockerComposeContainer<Nothing> by lazy {
        DockerComposeContainer<Nothing>(File("docker-compose.yml")).apply {
            withLocalCompose(true)
            withBuild(true)
            withServices(*services)
            withTailChildContainers(true)
        }
    }


    override fun beforeAll(ec: ExtensionContext?) {
        environment.start()
    }

    override fun afterAll(ec: ExtensionContext?) {
        environment.stop()
    }
}