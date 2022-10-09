package net.bellsoft.bellsafehouse.service

import org.springframework.stereotype.Service

@Service
class DummyService {
    fun echo(text: String = "dummy") = text
}
