rootProject.name = "BellSafeHouseAPI"

dependencyResolutionManagement {
    versionCatalogs {
        create("paketobuildpacks") {
            from(files("./gradle/paketobuildpacks.versions.toml"))
        }
    }
}
