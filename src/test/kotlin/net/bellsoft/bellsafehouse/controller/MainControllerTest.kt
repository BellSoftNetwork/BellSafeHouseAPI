package net.bellsoft.bellsafehouse.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
@AutoConfigureMockMvc
@ActiveProfiles("production")
internal class MainControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `displayIndex 요청 시 'Bell Safe House Index API' 반환`() {
        val uri = "/"

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Bell Safe House Index API"))
            .andDo(MockMvcResultHandlers.print())
    }
}
