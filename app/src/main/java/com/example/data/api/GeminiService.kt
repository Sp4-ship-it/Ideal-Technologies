package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val systemPrompt = """
        You are "Ideal Inside", the official AI Technology Consultant and customer support assistant for Ideal Technologies (idealtechnologies.co.zw), Zimbabwe's premier digital engineering and tech solutions provider.
        Motto: "Practical technology for ambitious people".
        WhatsApp & Hotline: +263 78 183 4073.
        Locations: Harare CBD (Joina City 4th Floor), Chinhoyi Technical Lab (Magamba Way), and Nationwide delivery.
        
        Exact Services & Pricing:
        - Web Development:
          * Startup / Single Page Website: $35 - $60 (one-time, responsive, WhatsApp button, 2-3 days delivery)
          * Small Business Website (3-5 pages): $60 - $100 (one-time, 4-6 days delivery)
          * Corporate Website (10-15 pages, CMS): $150 - $250 (one-time, 7-12 days delivery)
          * Custom Web Systems (CodeIgniter/PHP/Laravel, POS systems, portals): starts from $350+
        - Domains & Web Hosting:
          * .co.zw domain: $5 - $10 / year
          * International (.com, .org, .net): $15 - $20 / year
          * Starter Web Hosting: $3.50 / month ($35 / year) with cPanel, free SSL, 5 emails
          * Business Web Hosting: $7 - $15 / month with 50GB SSD, daily backups, unlimited emails
          * Custom Domain Email Setup (Zoho/Google Workspace): $20 setup fee
        - Branding & Graphics:
          * Digital Flyer / Social Banner: $10 - $20 / design
          * Full Brand Identity (Logo, Letterhead, Business Cards, Brand Guide): $50 - $100
          * Professional Company Profile PDF (6-12 pages): $35 - $75
        - Hardware & Repairs:
          * Laptop repairs (screens, motherboards, batteries, IC chips) with 24-48h turnaround and warranty.
          * Certified laptops for sale (HP EliteBook, Dell Latitude, Apple MacBook Air M1).
        - Payments: EcoCash USD (*151# Code 284910), InnBucks (*569#), OneMoney, Nostro USD bank transfer, Visa/Mastercard.

        Always be professional, polite, concise, and provide clear price breakdowns. Include recommendations and mention they can tap the WhatsApp button (+263 78 183 4073) or use the in-app Checkout anytime!
    """.trimIndent()

    suspend fun sendMessage(userPrompt: String, history: List<Pair<String, Boolean>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val hasValidKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (!hasValidKey) {
            return@withContext generateLocalResponse(userPrompt)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val contentsArray = JSONArray()

            // Add history
            for ((msg, isUser) in history.takeLast(6)) {
                val turn = JSONObject()
                turn.put("role", if (isUser) "user" else "model")
                val parts = JSONArray()
                parts.put(JSONObject().put("text", msg))
                turn.put("parts", parts)
                contentsArray.put(turn)
            }

            // Current prompt
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()
            currentParts.put(JSONObject().put("text", userPrompt))
            currentTurn.put("parts", currentParts)
            contentsArray.put(currentTurn)

            val rootJson = JSONObject()
            rootJson.put("contents", contentsArray)

            val systemInstruction = JSONObject()
            val sysParts = JSONArray()
            sysParts.put(JSONObject().put("text", systemPrompt))
            systemInstruction.put("parts", sysParts)
            rootJson.put("systemInstruction", systemInstruction)

            val body = rootJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.w("GeminiService", "API error: ${response.code} $responseString")
                return@withContext generateLocalResponse(userPrompt)
            }

            val respJson = JSONObject(responseString)
            val candidates = respJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (!text.isNullOrBlank()) {
                text.trim()
            } else {
                generateLocalResponse(userPrompt)
            }
        } catch (e: Exception) {
            Log.e("GeminiService", "Gemini call failed", e)
            generateLocalResponse(userPrompt)
        }
    }

    private fun generateLocalResponse(prompt: String): String {
        val q = prompt.lowercase()

        return when {
            q.contains("price") || q.contains("cost") || q.contains("how much") || q.contains("quote") -> {
                when {
                    q.contains("web") || q.contains("site") -> {
                        """
                        Here are our transparent Web Development packages at Ideal Technologies:

                        • Startup / Single Page: $35 - $60 (Turnaround: 2-3 Days)
                          Ideal for single products, promotions, or direct WhatsApp sales.
                        • Small Business Website (3-5 pages): $60 - $100 (Turnaround: 4-6 Days)
                          Includes Home, About, Services, Gallery, Contact & 1 month free support.
                        • Corporate Website (10-15 pages + CMS): $150 - $250 (Turnaround: 7-12 Days)
                          Full admin content management, document downloads, corporate emails.
                        • Custom Web Systems: Starts from $350+ (POS, Portals, CodeIgniter/PHP/Laravel).

                        Need a domain and hosting too? A .co.zw domain is only $5-$10/yr and Starter SSD Hosting is $3.50/mo ($35/yr)!
                        """.trimIndent()
                    }
                    q.contains("host") || q.contains("domain") -> {
                        """
                        Our Hosting & Domain Registration pricing:

                        • .co.zw Local Domain: $5 - $10 / year
                        • International (.com, .org, .net): $15 - $20 / year
                        • Starter Web Hosting: $3.50 / month ($35 / year)
                          Includes 10GB NVMe SSD, free AutoSSL, cPanel, and 5 business emails.
                        • Business Web Hosting: $7 - $15 / month
                          50GB SSD, daily automated backups, unlimited emails.
                        • Custom Domain Email Setup (Zoho/Google): $20 setup fee.

                        Instant setup with EcoCash, InnBucks, or Card payment in the app!
                        """.trimIndent()
                    }
                    q.contains("flyer") || q.contains("logo") || q.contains("brand") || q.contains("profile") -> {
                        """
                        Our Branding & Design pricing:

                        • Digital Flyer / Social Banner: $10 - $20 per design (High-res PNG & WhatsApp status size)
                        • Full Brand Identity Package: $50 - $100 (3 Logo concepts, letterhead, business cards, full vector files)
                        • Professional Company Profile (6-12 pages): $35 - $75 (Polished corporate PDF for tenders & clients)

                        Ready to start? You can order directly in the Services tab or message our design team on WhatsApp (+263 78 183 4073)!
                        """.trimIndent()
                    }
                    q.contains("repair") || q.contains("laptop") || q.contains("screen") -> {
                        """
                        Hardware Diagnostics & Repair Estimates:

                        • Diagnostic Check: Free initial bench triage
                        • Screen Replacement: $40 - $75 (Genuine OEM IPS screens)
                        • Battery Replacement: $35 - $65 (Certified Tier-1 cells)
                        • Motherboard Micro-Soldering (Charging IC / Short Circuits): $45 - $90
                        • High-Speed NVMe SSD & RAM Upgrades: From $30+

                        Average turnaround is 24 to 48 hours with guaranteed warranty. Track your ticket live in the Client Portal!
                        """.trimIndent()
                    }
                    else -> {
                        """
                        Ideal Technologies offers transparent, competitive rates:
                        
                        1. Web Design: $35 - $250 (Single page to full Corporate CMS)
                        2. Web Hosting: $3.50/mo ($35/yr) | .co.zw Domains: $5-$10/yr
                        3. Branding & Logos: $10 - $100 (Flyers, Logos, Company Profiles)
                        4. Custom Software & POS: Starts from $350+
                        5. Certified Hardware & Repairs: Certified laptops from $420, repairs from $25.

                        What project or device would you like an itemized estimate for?
                        """.trimIndent()
                    }
                }
            }
            q.contains("repair") || q.contains("broken") || q.contains("laptop") || q.contains("turn on") || q.contains("diagnostic") -> {
                """
                Our hardware repair lab operates in Harare (Joina City 4th Floor) and Chinhoyi (Magamba Way).
                
                Diagnostic & Repair Process:
                1. Intake & Ticket generation (Trackable in our app with ticket ID e.g., IT-8492).
                2. Component-level diagnostic check by senior hardware engineers.
                3. Quote approval via SMS/WhatsApp before any parts are installed.
                4. Quality testing & 6-month warranty on replaced components.

                You can tap 'Book Repair' in the Home tab right now to schedule an intake or courier pickup!
                """.trimIndent()
            }
            q.contains("contact") || q.contains("call") || q.contains("whatsapp") || q.contains("location") || q.contains("where") -> {
                """
                Connect with Ideal Technologies:
                
                • WhatsApp & Hotline: +263 78 183 4073
                • Email: info@idealtechnologies.co.zw / sales@idealtechnologies.co.zw
                • Harare Lab: 4th Floor, Joina City Innovation Hub, Harare CBD
                • Chinhoyi Lab: Magamba Way, Chinhoyi
                • Operating Hours: Mon - Sat: 8:00 AM - 5:30 PM (24/7 Server Monitoring)

                You can tap the WhatsApp or Call button directly from the Contact tab!
                """.trimIndent()
            }
            q.contains("hosting") || q.contains("domain") || q.contains(".co.zw") -> {
                """
                Yes! We offer official .co.zw domain registration ($5 - $10 / yr) and high-speed cPanel web hosting:
                
                • Starter Hosting: $3.50 / month ($35 / year) - Perfect for small businesses, blogs, and landing pages. Free SSL included.
                • Business Hosting: $7 - $15 / month - Designed for busy corporate websites with automated cloud backups.
                • International Domains (.com, .org, .net): $15 - $20 / yr.

                You can check domain availability and order directly in the Services tab.
                """.trimIndent()
            }
            q.contains("pay") || q.contains("ecocash") || q.contains("innbucks") || q.contains("currency") -> {
                """
                We accept all major Zimbabwean and international payment channels:
                
                • EcoCash USD (Dial *151# -> Merchant Code: 284910)
                • InnBucks USD (Dial *569# -> A/C: 0781834073)
                • OneMoney
                • Stanbic / CBZ USD Nostro & RTGS Bank Transfer
                • Visa / Mastercard

                Instant payment confirmation and receipts are generated right in the app!
                """.trimIndent()
            }
            else -> {
                """
                Hello! I am "Ideal Inside", your AI tech consultant at Ideal Technologies.
                
                I can assist you with:
                • Instant quote calculations for websites, software & mobile apps
                • Domain registration (.co.zw, .com) and hosting packages
                • Laptop diagnostics, repair booking & ticket tracking
                • Hardware sales (Certified HP EliteBooks, Dell Latitudes, MacBooks)
                • Graphic design, brand identity packages & corporate profiles
                
                How can I help power your ambitious project today?
                """.trimIndent()
            }
        }
    }
}
