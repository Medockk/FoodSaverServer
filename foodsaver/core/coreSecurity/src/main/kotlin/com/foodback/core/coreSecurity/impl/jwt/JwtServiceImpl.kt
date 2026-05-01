package com.foodback.core.coreSecurity.impl.jwt

import com.foodback.core.coreSecurity.api.service.JwtService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

/**
 * Special util to work with JWT tokens
 * @property jwtSecret Secret key to add a signature to JWT
 * @property expirationJwtMs expiration of access token in milliseconds
 * @property expirationRefreshMs expiration of refresh token in milliseconds
 */
@Service
internal class JwtServiceImpl: JwtService {

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
    override fun generateAccessToken(username: String): String {
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
    override fun generateRefreshToken(username: String): String {
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
    override fun getUsername(token: String): String {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (e: ExpiredJwtException) {
            e.claims.subject
        }
    }

    /**
     * Method to verify and validate JWT token
     * @return true, если токен валиден и ещё жив, иначе false
     */
    override fun validateToken(token: String): Boolean {
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