package com.foodback.config

import com.foodback.exception.handler.CustomAccessDeniedHandler
import com.foodback.exception.handler.CustomAuthenticationEntryPoint
import com.foodback.security.auth.UserDetailsServiceImpl
import com.foodback.security.csrf.CsrfTokenFilter
import com.foodback.security.jwt.JwtAuthenticationFilter
import com.foodback.security.jwt.JwtUtil
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
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsServiceImpl
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
     * [com.foodback.security.auth.UserDetailsImpl] - authentication user entity
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

        http
            .csrf { csrf -> csrf.disable() }
            .cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(authHandler)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth/**", "/media/**").permitAll()
                    // .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                    //.requestMatchers("/api/products").hasAnyRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(CsrfTokenFilter(accessDeniedHandler), JwtAuthenticationFilter::class.java)
        return http.build()
    }
}