package net.bellsoft.bellsafehouse.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping("/")
    fun displayIndex(): String {
        return "Bell Safe House Index API"
    }
}