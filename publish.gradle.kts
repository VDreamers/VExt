import java.io.FileInputStream
import java.util.*

apply(plugin = "maven-publish")
apply(plugin = "signing")

val fis = FileInputStream("local.properties")
val properties = Properties().apply {
    load(fis)
}
val sonatypeUsername: String? = properties.getProperty("sonatype.username")
val sonatypePassword: String? = properties.getProperty("sonatype.password")

val pkgGroupId: String by project
val pkgArtifactId: String by project
val pkgVersion: String by project

group = pkgGroupId
version = pkgVersion

fun getLocalRepositoryUrl(): java.net.URI {
    return uri("${rootDir}/.repos")
}

afterEvaluate {
    configure<PublishingExtension> {
        publications {
            val plugins = project.plugins
            when {
                plugins.hasPlugin("com.android.library") -> {
                    create<MavenPublication>("aar") {
                        from(components["release"])

                        tasks {
                            val androidSourcesJar by creating(Jar::class) {
                                archiveClassifier.set("sources")
                                from("android.sourceSets.main.java.srcDirs")
                            }
                            artifact(androidSourcesJar)
                        }

                        groupId = pkgGroupId
                        artifactId = pkgArtifactId
                        version = pkgVersion

                        VExt("aar")
                    }
                }
                plugins.hasPlugin("com.android.application") -> {
                    create<MavenPublication>("aab") {
                        from(components["release_aab"])

                        tasks {
                            val androidSourcesJar by creating(Jar::class) {
                                archiveClassifier.set("sources")
                                from("android.sourceSets.main.java.srcDirs")
                            }
                            artifact(androidSourcesJar)
                        }

                        groupId = pkgGroupId
                        artifactId = pkgArtifactId
                        version = pkgVersion

                        VExt("aab")
                    }
                }
                else -> {
                    create<MavenPublication>("jar") {
                        from(components["java"])

                        tasks {
                            val sourcesJar by creating(Jar::class) {
                                archiveClassifier.set("sources")
                                val sourceSets: SourceSetContainer by project
                                from(sourceSets["main"].allSource)
                            }
                            artifact(sourcesJar)
                            artifact(this["javadocJar"])
                        }

                        groupId = pkgGroupId
                        artifactId = pkgArtifactId
                        version = pkgVersion

                        VExt("jar")
                    }
                }
            }
        }
        repositories {
            maven {
                // 发布的位置，这里根据发布的版本区分了 LOCAL SNAPSHOT 和最终版本
                val localRepoUrl = getLocalRepositoryUrl()
                val releasesRepoUrl =
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl =
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(
                    if (version.toString().endsWith("LOCAL")) {
                        localRepoUrl
                    } else if (version.toString().endsWith("SNAPSHOT")) {
                        snapshotsRepoUrl
                    } else {
                        releasesRepoUrl
                    }
                )
                if (!version.toString().endsWith("LOCAL")) {
                    credentials {
                        // issues.sonatype.org 注册的账号
                        username = sonatypeUsername ?: ""
                        password = sonatypePassword ?: ""
                    }
                }
            }
        }
    }
}

configure<SigningExtension> {
    val publishing = extensions.getByName("publishing") as PublishingExtension
    sign(publishing.publications)
}

fun MavenPublication.VExt(pkg: String) {
    this.pom {
            packaging = pkg
            // 构件名称，可以自定义
            name.set("VExt")
            // 构件描述
            description.set("VExt is our ext fun collections")
            // 构件主页
            url.set("https://github.com/CodePoem/VExt")
            // 许可证名称和地址
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            // 开发者信息
            developers {
                developer {
                    name.set("CodePoem")
                    email.set("codepoemfun@gmail.com")
                }
            }
            // 版本控制仓库地址
            scm {
                url.set("https://github.com/CodePoem/VExt")
                connection.set("scm:git:github.com/CodePoem/VExt.git")
                developerConnection.set("scm:git:ssh://git@github.com/CodePoem/VExt.git")
            }
    }
}
