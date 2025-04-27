architectury {
    common {
        for(p in rootProject.subprojects) {
            if(p != project) {
                this@common.add(p.name)
            }
        }
    }
}

loom {
    accessWidenerPath = file("src/main/resources/bloodisfuel.accesswidener")
}

repositories {
    // mavens for Create Fabric and dependencies
    maven("https://mvn.devos.one/releases") // Porting Lib Releases
    maven("https://mvn.devos.one/snapshots/") // Create Fabric, Porting Lib, Forge Tags, Milk Lib, Registrate Fabric
    maven("https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // Forge config api port

    maven("https://jitpack.io/") // Mixin Extras, Fabric ASM

    maven("https://maven.shedaniel.me") // Cloth Config, REI
    maven("https://maven.blamejared.com") // JEI

    maven("https://api.modrinth.com/maven") // LazyDFU
    maven("https://maven.terraformersmc.com/releases/") // Mod Menu
}

dependencies {

    modImplementation("net.fabricmc:fabric-loader:${"fabric_loader_version"()}")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${"fabric_api_version"()}")

    modCompileOnly("com.simibubi.create:create-fabric-${"minecraft_version"()}:${"create_fabric_version"()}")

    modImplementation("dev.architectury:architectury:${"architectury_api_version"()}")
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-common:${"mixin_extras_version"()}")!!)
}

tasks.processResources {
    // don't add development or to-do files into built jar
    exclude("**/*.bbmodel", "**/*.lnk", "**/*.xcf", "**/*.md", "**/*.txt", "**/*.blend", "**/*.blend1")
}

sourceSets.main {
    resources { // include generated resources in resources
        srcDir("src/generated/resources")
        exclude("src/generated/resources/.cache")
    }
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}