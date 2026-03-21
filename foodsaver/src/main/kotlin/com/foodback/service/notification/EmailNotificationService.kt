package com.foodback.service.notification

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

/**
 * Special service to work with mail sender
 * @param javaMainSender special interface to send mail to gmail.com
 * @param webAddress Current server address, taken from application.yml
 * @param webPort Current server port, taken from application.yml
 */
@Service(value = "emailNotificationService")
class EmailNotificationService(
    private val javaMainSender: JavaMailSender,
    @Value($$"${server.web-address}")
    private val webAddress: String,
    @Value($$"${server.web-port}")
    private val webPort: String
): NotificationService {

    /**
     * Method to send token ([message]) to some user with email [recipient]
     * @param recipient the email address to send the [message]
     * @param message RESET-PASSWORD token
     */
    override fun sendNotification(recipient: String, message: String, metadata: Map<String, String>) {

        val message = javaMainSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        val frontendUrl = "http://$webAddress:$webPort/reset-password?id=$message"
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

        helper.setTo(recipient)
        helper.setSubject("Reset password")
        helper.setText(htmlContent, true)
        helper.setFrom("no-reply@foodsaver.com")

        javaMainSender.send(message)
    }
}