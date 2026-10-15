package com.example.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object IntentUtils {
    const val WHATSAPP_NUMBER = "+263781834073"
    const val PHONE_NUMBER = "+263781834073"
    const val EMAIL_ADDRESS = "info@idealtechnologies.co.zw"
    const val WEBSITE_URL = "https://idealtechnologies.co.zw"

    fun openWhatsApp(context: Context, customMessage: String = "Hello Ideal Technologies, I'd like to make an inquiry.") {
        try {
            val cleanPhone = WHATSAPP_NUMBER.replace("+", "").replace(" ", "")
            val encodedMsg = Uri.encode(customMessage)
            val url = "https://api.whatsapp.com/send?phone=$cleanPhone&text=$encodedMsg"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp: $WHATSAPP_NUMBER", Toast.LENGTH_LONG).show()
        }
    }

    fun dialPhone(context: Context, number: String = PHONE_NUMBER) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Call: $number", Toast.LENGTH_LONG).show()
        }
    }

    fun sendEmail(context: Context, subject: String = "Service Inquiry - Ideal Technologies", body: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$EMAIL_ADDRESS")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Email: $EMAIL_ADDRESS", Toast.LENGTH_LONG).show()
        }
    }

    fun openWebsite(context: Context, url: String = WEBSITE_URL) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
        }
    }
}
