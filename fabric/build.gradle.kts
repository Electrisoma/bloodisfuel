import me.modmuss50.mpp.ReleaseType
import java.util.*

architectury.fabric()

loom {
    val common = project(":common")
    accessWidenerPath = common.loom.accessWidenerPath

    runs {
        create("datagen") {
            client()

            name = "Minecraft Data"
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${common.file("src/generated/resources")}")
            vmArg("-Dfabric-api.datagen.modid=bloodisfuel")
            vmArg("-Dporting_lib.datagen.existing_resources=${common.file("src/main/resources")}")

            environmentVariable("DATAGEN", "TRUE")
        }

        getByName("client") {
            programArg("--username=Dev")
        }
    }
}

repositories {
    // mavens for Create Fabric and dependencies
    maven("https://mvn.devos.one/snapshots/") // Create Fabric, Porting Lib, Forge Tags, Milk Lib, Registrate Fabric
    maven("https://mvn.devos.one/releases") // Porting Lib Releases
    maven("https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // Forge config api port
    maven("https://jitpack.io/") // Mixin Extras, Fabric ASM
    maven("https://maven.shedaniel.me") // Cloth Config, REI
    maven("https://maven.blamejared.com") // JEI
    maven("https://maven.terraformersmc.com/releases") // Mod Menu, EMI
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${"fabric_loader_version"()}")
    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }

    // dependencies
    modImplementation("net.fabricmc.fabric-api:fabric-api:${"fabric_api_version"()}")
    modLocalRuntime("net.fabricmc.fabric-api:fabric-api-deprecated:${"fabric_api_version"()}")
    modImplementation("net.minecraftforge:forgeconfigapiport-fabric:4.2.11")

    // Create - dependencies are added transitively
    modImplementation("com.simibubi.create:create-fabric-${"minecraft_version"()}:${"create_fabric_version"()}")

    // Development QOL
    modLocalRuntime("com.terraformersmc:modmenu:${"modmenu_version"()}")

    // Recipe Viewers - Create Fabric supports JEI, REI, and EMI.
    // See root gradle.properties to choose which to use at runtime.
    when ("fabric_recipe_viewer".lowercase(Locale.ROOT)) {
        "jei" -> modLocalRuntime("mezz.jei:jei-${"minecraft_version"}-fabric:${"jei_version"}")
        "rei" -> modLocalRuntime("me.shedaniel:RoughlyEnoughItems-fabric:${"rei_version"}")
        "emi" -> modLocalRuntime("dev.emi:emi-fabric:${"emi_version"}")
        "disabled" -> {}
        else -> println("Unknown recipe viewer specified: ${"fabric_recipe_viewer"}. Must be JEI, REI, EMI, or disabled.")
    }

    // if you would like to add integration with them, uncomment them here.
//    modCompileOnly("mezz.jei:jei-$minecraft_version-fabric:$jei_version")
//    modCompileOnly("mezz.jei:jei-$minecraft_version-common:$jei_version")
//    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:$rei_version")
//    modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:$rei_version")
//    modCompileOnly("dev.emi:emi:$emi_version")
}

publishMods {
    file = tasks.remapJar.get().archiveFile
    version.set(project.version.toString())
    changelog = "sum stuff"
    type = ALPHA
    dryRun = System.getenv("DRYRUN")?.toBoolean() ?: true
    displayName = "Create: Blood is Fuel! ${"mod_version"()} Fabric ${"minecraft_version"()}"
    modLoaders.add("fabric")
    modLoaders.add("quilt")

    curseforge {
        projectId = "curseforge_id"()
        accessToken = System.getenv("CURSEFORGE_TOKEN")
        minecraftVersions.add("minecraft_version"())

        requires {
            slug = "create-fabric"
        }
    }

    modrinth {
        projectId = "modrinth_id"()
        accessToken = System.getenv("MODRINTH_TOKEN")
        minecraftVersions.add("minecraft_version"())

        requires {
            slug = "create-fabric"
        }
    }
}


operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}