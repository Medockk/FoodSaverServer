package com.foodback.service

import com.foodback.exception.auth.UserException
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.*

/**
 * Special service to work with mail sender
 * @param javaMainSender special interface to send mail to gmail.com
 * @param serverAddress Current server address, taken from application.yml
 * @param serverPort Current server port, taken from application.yml
 */
@Service
class DefaultEmailService(
    private val javaMainSender: JavaMailSender,
    @Value($$"${server.address}")
    private val serverAddress: String,
    @Value($$"${server.port}")
    private val serverPort: String
) {

    /**
     * Method to send [token] to [email]
     * @param email the email address to send the [token]
     * @param token RESET-PASSWORD token
     * @throws UserException if [email] is null or empty
     */
    fun sendMessage(email: String?, token: UUID) {

        if (email.isNullOrBlank()) throw UserException("Email must be not empty!", RequestError.UserRequest.EMPTY_EMAIL)

        val message = javaMainSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        val frontendUrl = "http://$serverAddress:$serverPort/reset-password?id=$token"
        val htmlContent = """
        <html>
        <body>
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>Reset password</h2>
                <p>You have requested a password reset. Click on the button below to continue:</p>
                
                <a href="$frontendUrl" 
                   style="background-color: #4CAF50; color: white; padding: 10px 15px; 
                          text-align: center; text-decoration: none; display: inline-block; 
                          border-radius: 5px; font-weight: bold;">
                    Reset password
                </a>
                
                <p style="margin-top: 20px; font-size: 12px; color: #888;">
                    This link expired at 30 minute.
                </p>
            </div>
        </body>
        </html>
    """.trimIndent()

        helper.setTo(email)
        helper.setSubject("Reset password")
        helper.setText(htmlContent, true)
        helper.setFrom("no-reply@foodsaver.com")

        javaMainSender.send(message)
    }
}