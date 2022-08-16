package net.bellsoft.bellsafehouse.controller

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MainController(
    @Value("\${application.deploy.environment}") val applicationDeployEnvironment: String
) {

    @GetMapping("/")
    fun displayIndex(): String {
        if (false)
            println("no covered")
        logger.info("API Call")

        return "Bell Safe House Index API ($applicationDeployEnvironment)"
    }

    @GetMapping("/environment")
    fun displayEnvironment(): String {
        logger.info("Application Deploy Environment API Call")

        return applicationDeployEnvironment
    }

    companion object : KLogging()
}
