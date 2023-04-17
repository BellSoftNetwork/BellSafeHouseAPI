package net.bellsoft.bellsafehouse.config

import net.bellsoft.bellsafehouse.component.UserTokenProvider
import net.bellsoft.bellsafehouse.component.jwt.JwtSupport
import net.bellsoft.bellsafehouse.filter.JwtFilter
import net.bellsoft.bellsafehouse.service.auth.AuthService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity(debug = false)
class WebSecurityConfig(
    private val jwtSupport: JwtSupport,
    private val userTokenProvider: UserTokenProvider,
    private val authService: AuthService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.run {
            authorizeHttpRequests {
                it.requestMatchers("/*/auth/**").permitAll()
                it.requestMatchers("/*/check/**").permitAll()
                it.requestMatchers("/docs/**").permitAll()
                it.anyRequest().authenticated()
            }
            addFilterAfter(
                JwtFilter(jwtSupport, userTokenProvider, authService),
                UsernamePasswordAuthenticationFilter::class.java,
            )
            sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            csrf {
                it.disable()
            }
            formLogin {
                it.disable()
            }
        }.build()
    }
}
