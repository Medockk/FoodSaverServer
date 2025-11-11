package com.foodback.demo.service

import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.exception.general.ErrorCode.RequestError
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultEmailService(
    private val javaMainSender: JavaMailSender
) {

    fun sendMassage(email: String?, token: UUID) {

        if (email.isNullOrBlank()) throw UserException("Email must be not empty!", RequestError.UserRequest.EMPTY_EMAIL)

        val message = javaMainSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        val frontendUrl = "http://172.19.0.1:8080/reset-password?id=$token"
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