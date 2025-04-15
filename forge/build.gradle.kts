import dev.ithundxr.silk.ChangelogText

architectury.forge()

loom {
    accessWidenerPath = project(":common").loom.accessWidenerPath

    forge {
        mixinConfig("bloodisfuel-common.mixins.json")
        mixinConfig("bloodisfuel.mixins.json")

        convertAccessWideners = true
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}

repositories {
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/"){
        name = "Geckolib Maven"
    }
    maven("https://maven.theillusivec4.top/") // Curios
    maven("https://maven.terraformersmc.com/releases/") // EMI
    maven("https://jitpack.io/") // Mixin Extras, Fabric ASM
    maven("https://maven.tterrag.com/") { // Create Forge and Registrate Forge
        content {
            includeGroup("com.tterrag.registrate")
            includeGroup("com.simibubi.create")
        }
    }
}

dependencies {
    forge("net.minecraftforge:forge:${"minecraft_version"()}-${"forge_version"()}")
    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }

    // Create and its dependencies
    modImplementation("com.simibubi.create:create-${"minecraft_version"()}:${"create_forge_version"()}:slim") { isTransitive = false }
    modImplementation("net.createmod.ponder:Ponder-Forge-${"minecraft_version"()}:${"ponder_version"()}")
    modImplementation("com.tterrag.registrate:Registrate:${"registrate_forge_version"()}")
    modCompileOnly("dev.engine-room.flywheel:flywheel-forge-api-${"minecraft_version"()}:${"flywheel_version"()}")
    modRuntimeOnly("dev.engine-room.flywheel:flywheel-forge-${"minecraft_version"()}:${"flywheel_version"()}")

    // Mixin
    compileOnly("io.github.llamalad7:mixinextras-common:${"mixin_extras_version"()}")
    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-forge:${"mixin_extras_version"()}")!!)!!)

    // Content Testing
//    modLocalRuntime("curse.maven:create-diesel-generators-869316:6286179")

//    modLocalRuntime("maven.modrinth:biomancy:${biomancy_version}")
//    modLocalRuntime("maven.modrinth:biofactory:${biofactory_version}")
//
//    modLocalRuntime("software.bernie.geckolib:geckolib-forge-${minecraft_version}:${geckolib_version}")
//    forgeRuntimeLibrary("com.eliotlash.mclib:mclib:20")
//    //https://h2database.com/html/mvstore.html
//    loaderLibraries(group: "com.h2database", name: "h2-mvstore", version: "2.2.224")
//    (group: "com.h2database", name: "h2-mvstore", version: "[2.2.220,3.0.0)")

    modLocalRuntime("maven.modrinth:alexs-caves:2.0.2")
    modLocalRuntime("maven.modrinth:citadel:2.6.0")

    modLocalRuntime("maven.modrinth:farmers-delight:1.20.1-1.2.5")


    // Development QOL
    modLocalRuntime("mezz.jei:jei-${"minecraft_version"()}-forge:${"jei_version"()}") { isTransitive = false }

    modLocalRuntime("curse.maven:embeddium-908741:5681725")

    // if you would like to add integration with JEI, uncomment this line.
//    modCompileOnly("mezz.jei:jei-${minecraft_version}-forge-api:${jei_version}")
}

// Modmuss Publish
publishMods {
    file = tasks.remapJar.get().archiveFile
    version.set(project.version.toString())
    changelog = ChangelogText.getChangelogText(rootProject).toString()
    type = ALPHA
    dryRun = System.getenv("DRYRUN")?.toBoolean() ?: true
    displayName = "${"mod_name_full"()} ${"mod_version"()} Forge ${"minecraft_version"()}"
    modLoaders.add("forge")
    modLoaders.add("neoforge")

    curseforge {
        projectId = "curseforge_id"()
        accessToken = System.getenv("CURSEFORGE_TOKEN")
        minecraftVersions.add("minecraft_version"())

        requires {
            slug = "create"
        }
    }

    modrinth {
        projectId = "modrinth_id"()
        accessToken = System.getenv("MODRINTH_TOKEN")
        minecraftVersions.add("minecraft_version"())

        requires {
            slug = "create"
        }
    }
}

// If the property is not set; :person_shrugging:
operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}