package me.modmuss50.mpp

import me.modmuss50.mpp.platforms.curseforge.CurseForge
import org.gradle.api.Action
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import java.nio.file.Path

abstract class ModPublishExtension(val project: Project) : PublishOptions {
    abstract val dryRun: Property<Boolean>
    abstract val maxRetries: Property<Int>
    val platforms: ExtensiblePolymorphicDomainObjectContainer<Platform> = project.objects.polymorphicDomainObjectContainer(Platform::class.java)

    init {
        dryRun.convention(false)
        maxRetries.convention(3)

        // Inherit the platform options from this extension.
        // TODO we may need to do this in the platform factory
        platforms.whenObjectAdded {
            it.from(this)
        }
    }

    fun curseForge(name: String = "curseForge", action: Action<CurseForge>): NamedDomainObjectProvider<CurseForge> {
        return platforms.register(name, CurseForge::class.java, action)
    }
}

internal val Project.modPublishExtension: ModPublishExtension
    get() = extensions.getByType(ModPublishExtension::class.java)

internal val RegularFileProperty.path: Path
    get() = get().asFile.toPath()