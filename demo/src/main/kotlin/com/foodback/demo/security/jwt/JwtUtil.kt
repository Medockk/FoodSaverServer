package com.foodback.demo.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.util.*

/**
 * Special util to work with JWT tokens
 * @property jwtSecret Secret key to add a signature to JWT
 * @property expirationJwtMs expiration of access token in milliseconds
 * @property expirationRefreshMs expiration of refresh token in milliseconds
 */
@Configuration
class JwtUtil {

    @Value($$"${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value($$"${jwt.expiration.ms}")
    private var expirationJwtMs: Long = 0L

    @Value($$"${jwt.refresh.expiration.ms}")
    private var expirationRefreshMs: Long = 0L

    private val key by lazy {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    }

    /**
     * Method to generate access token for [username]
     * @param username unique username
     */
    fun generateAccessToken(username: String): String {
        println("JWT expires time: $expirationJwtMs")
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationJwtMs))
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    /**
     * Method to generate refresh token for [username]
     * @param username unique username
     */
    fun generateRefreshToken(username: String): String {
        return Jwts.builder()
            .signWith(key, Jwts.SIG.HS256)
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationRefreshMs))
            .compact()
    }

    /**
     * Method to get unique username from [token]
     * @param token access token
     */
    fun getUsername(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    /**
     * Method to verify and validate JWT token
     */
    fun validate(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(key).build().parse(token)
            return true
        } catch (e: SignatureException) {
            println("INVALID JWT SIGNATURE\n${e.message}")
        } catch (e: SecurityException) {
            println("SECURITY JWT EXCEPTION\n${e.message}")
        } catch (e: ExpiredJwtException) {
            println("JWT TOKEN EXPIRED\n${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }
}