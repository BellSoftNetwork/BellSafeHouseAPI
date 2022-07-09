package net.bellsoft.bellsafehouse

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BellSafeHouseApplicationTests {

    @Test
    fun `예제 테스트`() {
        Assertions.assertThat(true).isEqualTo(true) // 두 값이 다르면 Test 스테이지가 중단됨.
    }
}
