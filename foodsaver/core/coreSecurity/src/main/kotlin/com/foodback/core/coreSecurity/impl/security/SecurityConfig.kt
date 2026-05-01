package com.foodback.core.coreSecurity.impl.security

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipalSource
import com.foodback.core.coreSecurity.api.service.JwtService
import com.foodback.core.coreSecurity.api.service.SecurityConfigurationCustomizer
import com.foodback.core.coreSecurity.impl.csrf.CsrfTokenFilter
import com.foodback.core.coreSecurity.impl.error.CustomAccessDeniedHandler
import com.foodback.core.coreSecurity.impl.error.CustomAuthenticationEntryPoint
import com.foodback.core.coreSecurity.impl.jwt.JwtAuthenticationFilter
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Security class to configure permissions and main security configurations
 */
@Configuration
internal class SecurityConfig(
    private val jwtUtil: JwtService,
    private val userDetailsService: SecurityPrincipalSource,
    private val securityConfigurationCustomizers: List<SecurityConfigurationCustomizer>
) {

    /**
     * Method to provide Single instance of JwtAuthenticationFilter
     */
    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtUtil, userDetailsService)
    }

    /**
     * Method to get [AuthenticationManager] - special manager to authenticate user and get
     * [com.foodback.config.security.auth.UserDetailsImpl] - authentication user entity
     */
    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }

    /**
     * Method to get [PasswordEncoder] - spacial crypto util to convert password to hash
     */
    @Bean
    @Primary
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers("/media/**")
        }
    }

    /**
     * Method to authorize HTTP requests for all users or some roles,
     * add JwtAuthenticationFilter before [UsernamePasswordAuthenticationFilter],
     * disable csrf check, set session creation policy,
     * and add Exception handler for 401 and 403 HTTP-error
     */
    @Bean
    fun filterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        authHandler: CustomAuthenticationEntryPoint,
        accessDeniedHandler: CustomAccessDeniedHandler
    ): SecurityFilterChain {

        val publicPaths = securityConfigurationCustomizers
            .flatMap { it.getPublicPaths() }
            .toTypedArray()

        http
            .csrf { csrf -> csrf.disable() }
            .cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(authHandler)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .authorizeHttpRequests {
                it.dispatcherTypeMatchers(DispatcherType.ASYNC)
                    .permitAll()
                it.requestMatchers(*publicPaths).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(CsrfTokenFilter(accessDeniedHandler, securityConfigurationCustomizers), JwtAuthenticationFilter::class.java)
        return http.build()
    }
}