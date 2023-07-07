package me.modmuss50.mpp.test

import org.gradle.testkit.runner.TaskOutcome
import kotlin.test.Test
import kotlin.test.assertEquals

class CurseForgeTest : IntegrationTest {
    @Test
    fun uploadCurseForge() {
        val server = MockWebServer(MockCurseForgeApi())

        val result = gradleTest()
            .buildScript(
                """
                import me.modmuss50.mpp.PublishOptions
                import me.modmuss50.mpp.platforms.curseforge.CurseForge

                publishMods {
                    file = tasks.jar.flatMap { it.archiveFile } // TODO is this really the best way?
                    changelog = "Hello!"
                    version = "1.0.0"
                    type = PublishOptions.ReleaseType.BETA

                    curseForge {
                        accessToken = "123"
                        projectId = "123456"
                        minecraftVersions.add("1.20.1")
                        
                        apiEndpoint = "${server.endpoint}"
                    }
                }
                """.trimIndent(),
            )
            .run("publishCurseForge")

        assertEquals(TaskOutcome.SUCCESS, result.task(":publishCurseForge")!!.outcome)
    }
}