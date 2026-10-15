package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class IdealRepository {

    private val _services = MutableStateFlow<List<ServiceItem>>(emptyList())
    val services: StateFlow<List<ServiceItem>> = _services.asStateFlow()

    private val _hardwareItems = MutableStateFlow<List<HardwareItem>>(emptyList())
    val hardwareItems: StateFlow<List<HardwareItem>> = _hardwareItems.asStateFlow()

    private val _repairTickets = MutableStateFlow<List<RepairTicket>>(emptyList())
    val repairTickets: StateFlow<List<RepairTicket>> = _repairTickets.asStateFlow()

    private val _hostingSubscriptions = MutableStateFlow<List<HostingSubscription>>(emptyList())
    val hostingSubscriptions: StateFlow<List<HostingSubscription>> = _hostingSubscriptions.asStateFlow()

    private val _transactions = MutableStateFlow<List<PaymentTransaction>>(emptyList())
    val transactions: StateFlow<List<PaymentTransaction>> = _transactions.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _services.value = listOf(
            // Web & Software Development
            ServiceItem(
                id = "web_startup",
                category = ServiceCategory.WEB_AND_SOFTWARE,
                title = "Startup / Single Page Website",
                priceDescription = "$35 - $60",
                minPrice = 35.0,
                maxPrice = 60.0,
                billingType = BillingType.ONE_TIME,
                shortDescription = "High-converting landing page optimized for mobile, WhatsApp lead conversion, and local SEO.",
                features = listOf(
                    "1 Clean Modern Landing Page",
                    "Mobile & Tablet Responsive",
                    "Direct WhatsApp Chat Integration",
                    "Google Maps & Contact Form",
                    "Basic On-Page SEO Setup",
                    "Free SSL Certificate Setup"
                ),
                popular = true,
                turnaroundTime = "2-3 Days"
            ),
            ServiceItem(
                id = "web_smallbiz",
                category = ServiceCategory.WEB_AND_SOFTWARE,
                title = "Small Business Website (3-5 Pages)",
                priceDescription = "$60 - $100",
                minPrice = 60.0,
                maxPrice = 100.0,
                billingType = BillingType.ONE_TIME,
                shortDescription = "Professional corporate website for growing enterprises, service firms, and SMEs.",
                features = listOf(
                    "3 to 5 Custom Styled Pages",
                    "Home, About, Services, Gallery, Contact",
                    "Speed & Mobile Optimization",
                    "Blog / News Section Ready",
                    "Social Media & WhatsApp Linking",
                    "1 Month Free Technical Support"
                ),
                popular = true,
                turnaroundTime = "4-6 Days"
            ),
            ServiceItem(
                id = "web_corporate",
                category = ServiceCategory.WEB_AND_SOFTWARE,
                title = "Corporate Website (10-15 Pages, CMS)",
                priceDescription = "$150 - $250",
                minPrice = 150.0,
                maxPrice = 250.0,
                billingType = BillingType.ONE_TIME,
                shortDescription = "Full-featured portal with intuitive admin content management, staff portals, and multi-service showcases.",
                features = listOf(
                    "10 to 15 Structured Content Pages",
                    "Self-Service Admin CMS Panel",
                    "Client Document / Resource Downloads",
                    "Advanced Search Engine Optimization",
                    "Corporate Email Accounts Setup",
                    "Analytics & Lead Tracking Dashboard"
                ),
                turnaroundTime = "7-12 Days"
            ),
            ServiceItem(
                id = "web_custom_systems",
                category = ServiceCategory.WEB_AND_SOFTWARE,
                title = "Custom Web Systems & Software",
                priceDescription = "Starts from $350+",
                minPrice = 350.0,
                maxPrice = null,
                billingType = BillingType.ONE_TIME,
                shortDescription = "Tailored bespoke web applications: POS systems, school portals, inventory trackers, and billing platforms.",
                features = listOf(
                    "Robust PHP / CodeIgniter / Laravel Stack",
                    "Role-Based Access Control (Admin, Staff, Client)",
                    "Custom Reporting & PDF Invoicing",
                    "Automated SMS / Email Notifications",
                    "Local Payment Gateway Integration (EcoCash/Paynow)",
                    "Full Database Architecture & Backups"
                ),
                turnaroundTime = "14-30 Days"
            ),

            // Domains & Web Hosting
            ServiceItem(
                id = "domain_cozw",
                category = ServiceCategory.HOSTING_AND_DOMAINS,
                title = ".co.zw Local Domain Registration",
                priceDescription = "$5 - $10 / year",
                minPrice = 5.0,
                maxPrice = 10.0,
                billingType = BillingType.ANNUAL,
                shortDescription = "Official Zimbabwean top-level country code domain with instant DNS registration.",
                features = listOf(
                    "Full DNS Zone Management",
                    "Zero Hidden Transfer Lock Fees",
                    "Nameserver Redirection",
                    "Local Zimbabwean Identity",
                    "Annual Renewal Reminder Alerts"
                ),
                popular = true,
                turnaroundTime = "Instant - 24 Hours"
            ),
            ServiceItem(
                id = "domain_intl",
                category = ServiceCategory.HOSTING_AND_DOMAINS,
                title = "International Domain (.com, .org, .net)",
                priceDescription = "$15 - $20 / year",
                minPrice = 15.0,
                maxPrice = 20.0,
                billingType = BillingType.ANNUAL,
                shortDescription = "Global domain extension for international reach and worldwide customer trust.",
                features = listOf(
                    "Free WHOIS Privacy Protection",
                    "Instant Activation",
                    "Easy Subdomain Management",
                    "DNSSEC Security Support"
                ),
                turnaroundTime = "Instant Activation"
            ),
            ServiceItem(
                id = "hosting_starter",
                category = ServiceCategory.HOSTING_AND_DOMAINS,
                title = "Starter Web Hosting (cPanel)",
                priceDescription = "$3.50 / month ($35 / year)",
                minPrice = 3.50,
                maxPrice = 35.0,
                billingType = BillingType.MONTHLY,
                shortDescription = "High-speed SSD hosting tailored for single websites and personal brands.",
                features = listOf(
                    "10GB NVMe SSD Storage",
                    "Free Unlimited AutoSSL",
                    "5 Custom Professional Email Accounts",
                    "cPanel Control Panel",
                    "99.9% Uptime Guarantee",
                    "1-Click WordPress Installer"
                ),
                popular = true,
                turnaroundTime = "Instant Setup"
            ),
            ServiceItem(
                id = "hosting_business",
                category = ServiceCategory.HOSTING_AND_DOMAINS,
                title = "Business Web Hosting",
                priceDescription = "$7 - $15 / month",
                minPrice = 7.0,
                maxPrice = 15.0,
                billingType = BillingType.MONTHLY,
                shortDescription = "Unmetered bandwidth, priority CPU allocation, and automated daily cloud snapshots.",
                features = listOf(
                    "50GB High-Speed SSD Storage",
                    "Unlimited Custom Domain Emails",
                    "Daily Remote Backups & Restore",
                    "Free Website Migration Assistance",
                    "Enhanced DDoS & Firewall Protection",
                    "Priority WhatsApp Tech Support"
                ),
                turnaroundTime = "Instant Setup"
            ),
            ServiceItem(
                id = "email_custom_setup",
                category = ServiceCategory.HOSTING_AND_DOMAINS,
                title = "Custom Domain Email Setup",
                priceDescription = "$20 setup fee",
                minPrice = 20.0,
                maxPrice = 20.0,
                billingType = BillingType.SETUP_FEE,
                shortDescription = "Setup of corporate emails on Zoho Mail or Google Workspace with DKIM, SPF & DMARC.",
                features = listOf(
                    "SPF, DKIM & DMARC DNS Records",
                    "Zero Spam Guarantee Setup",
                    "Mobile Phone & Outlook Sync Guide",
                    "Up to 5 User Accounts Configured",
                    "Signature Template Design"
                ),
                turnaroundTime = "Same Day"
            ),

            // Branding, Graphics & Digital Marketing
            ServiceItem(
                id = "design_flyer",
                category = ServiceCategory.BRANDING_AND_MARKETING,
                title = "Digital Flyer / Social Banner",
                priceDescription = "$10 - $20 / design",
                minPrice = 10.0,
                maxPrice = 20.0,
                billingType = BillingType.ONE_TIME,
                shortDescription = "Eye-catching graphic artwork for WhatsApp status, Instagram, Facebook, and printing.",
                features = listOf(
                    "High-Resolution PNG / JPEG & PDF",
                    "WhatsApp Status (9:16) & Square (1:1) Sizes",
                    "2 Free Rounds of Revisions",
                    "Stock Imagery & Vector Graphics Included"
                ),
                popular = true,
                turnaroundTime = "24-48 Hours"
            ),
            ServiceItem(
                id = "design_brand_identity",
                category = ServiceCategory.BRANDING_AND_MARKETING,
                title = "Full Brand Identity Package",
                priceDescription = "$50 - $100",
                minPrice = 50.0,
                maxPrice = 100.0,
                billingType = BillingType.ONE_TIME,
                shortDescription = "Complete corporate visual identity: Primary logo, letterhead, business cards, and brand guide.",
                features = listOf(
                    "3 Unique Logo Design Concepts",
                    "Editable Vector Files (AI, SVG, PDF, PNG)",
                    "Corporate Letterhead (Word & PDF)",
                    "Double-Sided Business Card Artwork",
                    "Brand Color Palette & Typography Guide"
                ),
                popular = true,
                turnaroundTime = "3-5 Days"
            ),
            ServiceItem(
                id = "design_company_profile",
                category = ServiceCategory.BRANDING_AND_MARKETING,
                title = "Professional Company Profile (6-12 Pages)",
                priceDescription = "$35 - $75",
                minPrice = 35.0,
                maxPrice = 75.0,
                billingType = BillingType.ONE_TIME,
                shortDescription = "Compelling corporate profile for tenders, corporate proposals, and client presentations.",
                features = listOf(
                    "6 to 12 Custom Layout Pages",
                    "Executive Summary, Vision, Services & Team",
                    "Client Proofreading & Copy Polish",
                    "High-Quality Print & Web-Optimized PDF",
                    "QR Code to Website & WhatsApp"
                ),
                turnaroundTime = "3-5 Days"
            )
        )

        _hardwareItems.value = listOf(
            HardwareItem(
                id = "hw_hp_840",
                name = "HP EliteBook 840 G8",
                category = HardwareCategory.LAPTOPS,
                priceUsd = 480.0,
                originalPriceUsd = 550.0,
                specs = listOf(
                    "Intel Core i7 11th Gen (up to 4.8GHz)",
                    "16GB DDR4 RAM",
                    "512GB NVMe M.2 SSD",
                    "14.0\" FHD IPS Anti-Glare Display",
                    "Backlit Keyboard + Fingerprint Reader",
                    "Bang & Olufsen Premium Audio"
                ),
                condition = "Grade A+ Pristine Business Refurb",
                inStock = true,
                stockCount = 4,
                warrantyMonths = 12,
                isFeatured = true
            ),
            HardwareItem(
                id = "hw_dell_latitude",
                name = "Dell Latitude 5420",
                category = HardwareCategory.LAPTOPS,
                priceUsd = 420.0,
                originalPriceUsd = 470.0,
                specs = listOf(
                    "Intel Core i5 11th Gen vPro",
                    "16GB DDR4 3200MHz RAM",
                    "256GB PCIe NVMe SSD",
                    "14.0\" Anti-Glare FHD Display",
                    "Thunderbolt 4 / USB-C Fast Charge",
                    "Heavy-Duty Enterprise Build"
                ),
                condition = "Certified Refurbished",
                inStock = true,
                stockCount = 6,
                warrantyMonths = 6,
                isFeatured = true
            ),
            HardwareItem(
                id = "hw_macbook_air",
                name = "Apple MacBook Air M1",
                category = HardwareCategory.LAPTOPS,
                priceUsd = 720.0,
                originalPriceUsd = 799.0,
                specs = listOf(
                    "Apple M1 8-Core CPU / 7-Core GPU",
                    "8GB Unified Memory",
                    "256GB Ultra-Fast SSD",
                    "13.3\" Retina True Tone Display",
                    "Up to 18 Hours Battery Life",
                    "Space Gray / Pristine Battery Health (96%+)"
                ),
                condition = "Certified Pre-Owned",
                inStock = true,
                stockCount = 2,
                warrantyMonths = 6,
                isFeatured = true
            ),
            HardwareItem(
                id = "hw_mikrotik_hex",
                name = "MikroTik hEX Gigabit Router (RB750Gr3)",
                category = HardwareCategory.NETWORKING,
                priceUsd = 75.0,
                specs = listOf(
                    "5x Gigabit Ethernet Ports",
                    "Dual Core 880MHz CPU",
                    "Full RouterOS v7 License",
                    "Bandwidth Queue & Hotspot Management",
                    "Hardware IPsec Encryption Engine"
                ),
                condition = "Brand New in Box",
                inStock = true,
                stockCount = 12,
                warrantyMonths = 12,
                isFeatured = true
            ),
            HardwareItem(
                id = "hw_tplink_ax55",
                name = "TP-Link Archer AX55 Wi-Fi 6 Router",
                category = HardwareCategory.NETWORKING,
                priceUsd = 95.0,
                specs = listOf(
                    "Next-Gen 3 Gbps Speeds (2402 Mbps on 5GHz)",
                    "OFDMA & MU-MIMO for 50+ Devices",
                    "4 High-Gain Antennas + Beamforming",
                    "HomeShield Security Protection",
                    "Ideal for Fiber & High-Speed Offices"
                ),
                condition = "Brand New in Box",
                inStock = true,
                stockCount = 8,
                warrantyMonths = 12,
                isFeatured = false
            ),
            HardwareItem(
                id = "hw_samsung_nvme_1tb",
                name = "Samsung 980 NVMe SSD 1TB",
                category = HardwareCategory.STORAGE,
                priceUsd = 85.0,
                specs = listOf(
                    "PCIe 3.0 x4 M.2 2280 Form Factor",
                    "Read Speeds up to 3,500 MB/s",
                    "Intelligent TurboWrite Technology",
                    "Free Installation & OS Clone at Lab"
                ),
                condition = "Brand New",
                inStock = true,
                stockCount = 15,
                warrantyMonths = 24,
                isFeatured = false
            ),
            HardwareItem(
                id = "hw_gan_charger",
                name = "65W GaN Dual USB-C Fast Charger",
                category = HardwareCategory.ACCESSORIES,
                priceUsd = 35.0,
                specs = listOf(
                    "65W Power Delivery for Laptops (Dell, HP, Mac)",
                    "2x USB-C + 1x USB-A Ports",
                    "Ultra-Compact GaN Semiconductor",
                    "Includes 100W Braided USB-C Cable"
                ),
                condition = "Brand New",
                inStock = true,
                stockCount = 20,
                warrantyMonths = 12,
                isFeatured = true
            ),
            HardwareItem(
                id = "hw_logitech_combo",
                name = "Logitech MK295 Silent Wireless Combo",
                category = HardwareCategory.ACCESSORIES,
                priceUsd = 45.0,
                specs = listOf(
                    "SilentTouch 90% Noise Reduction",
                    "2.4GHz Reliable Nano USB Receiver",
                    "Spill-Resistant Keyboard Design",
                    "Up to 36-Month Battery Longevity"
                ),
                condition = "Brand New",
                inStock = true,
                stockCount = 10,
                warrantyMonths = 12,
                isFeatured = false
            )
        )

        _repairTickets.value = listOf(
            RepairTicket(
                ticketId = "IT-8492",
                clientName = "Farai Chitepo",
                deviceModel = "HP Spectre x360 14\" (2022)",
                serialNumber = "5CD14892KL",
                intakeDate = "12 Oct 2026",
                estimatedCompletion = "16 Oct 2026",
                currentStage = RepairStage.PARTS_SOURCING,
                reportedIssue = "Laptop completely dead, zero LED lights when plugged into AC charger.",
                technicianFindings = "Short circuit detected on 19V primary power rail. Primary charging IC (ISL95522) faulty. Replacement microchip sourced and bench testing scheduled.",
                laborCostUsd = 45.0,
                partsCostUsd = 30.0,
                technicianName = "Tinashe M. (Senior Micro-Soldering Tech)",
                location = "Harare Lab - Joina City 4th Floor"
            ),
            RepairTicket(
                ticketId = "IT-9104",
                clientName = "Ruvimbo Mhere",
                deviceModel = "Dell Latitude 5420",
                serialNumber = "8K91PQ2",
                intakeDate = "13 Oct 2026",
                estimatedCompletion = "15 Oct 2026 (Today)",
                currentStage = RepairStage.REPAIRING,
                reportedIssue = "Cracked LCD panel after accidental drop, right hinge stiff.",
                technicianFindings = "Damaged 14.0\" FHD 30-pin IPS panel. Hinge mount reinforced with industrial resin. Installing genuine OEM matte screen.",
                laborCostUsd = 25.0,
                partsCostUsd = 65.0,
                technicianName = "Kudzai D. (Hardware Specialist)",
                location = "Harare Lab - Joina City 4th Floor"
            ),
            RepairTicket(
                ticketId = "IT-7721",
                clientName = "Nyasha Gumbo",
                deviceModel = "MacBook Pro 13\" M1 (A2338)",
                serialNumber = "C02F830NQ05D",
                intakeDate = "10 Oct 2026",
                estimatedCompletion = "14 Oct 2026",
                currentStage = RepairStage.READY_FOR_PICKUP,
                reportedIssue = "Service Battery alert, battery swelling slightly lifting trackpad.",
                technicianFindings = "Original cell degraded to 64% capacity. Replaced with certified Tier-1 A2338 battery pack. Thermal paste refreshed with Arctic MX-6. All Apple diagnostic tests passed.",
                laborCostUsd = 35.0,
                partsCostUsd = 75.0,
                technicianName = "Tinashe M.",
                location = "Harare Lab - Joina City 4th Floor"
            )
        )

        _hostingSubscriptions.value = listOf(
            HostingSubscription(
                domainName = "zimbabwetravel.co.zw",
                planName = "Starter Web Hosting + .co.zw",
                registrationDate = "10 Nov 2025",
                expirationDate = "10 Nov 2026",
                daysRemaining = 26,
                annualFeeUsd = 45.0,
                sslActive = true,
                storageUsageGb = 2.4,
                storageTotalGb = 10.0
            ),
            HostingSubscription(
                domainName = "hararelogistics.com",
                planName = "Business Web Hosting + .com",
                registrationDate = "22 Oct 2025",
                expirationDate = "22 Oct 2026",
                daysRemaining = 7,
                annualFeeUsd = 95.0,
                sslActive = true,
                storageUsageGb = 18.2,
                storageTotalGb = 50.0
            ),
            HostingSubscription(
                domainName = "farmfreshproduce.co.zw",
                planName = ".co.zw Domain Only",
                registrationDate = "15 Jan 2026",
                expirationDate = "15 Jan 2027",
                daysRemaining = 92,
                annualFeeUsd = 10.0,
                sslActive = true,
                storageUsageGb = 0.0,
                storageTotalGb = 0.0
            )
        )
    }

    fun addRepairTicket(ticket: RepairTicket) {
        _repairTickets.value = listOf(ticket) + _repairTickets.value
    }

    fun addTransaction(transaction: PaymentTransaction) {
        _transactions.value = listOf(transaction) + _transactions.value
    }

    fun findTicket(query: String): RepairTicket? {
        val q = query.trim().lowercase()
        return _repairTickets.value.firstOrNull {
            it.ticketId.lowercase().contains(q) ||
                    it.clientName.lowercase().contains(q) ||
                    it.deviceModel.lowercase().contains(q) ||
                    it.serialNumber.lowercase().contains(q)
        }
    }

    fun checkDomainAvailability(domain: String): Pair<Boolean, Double> {
        val clean = domain.trim().lowercase()
        val takenList = listOf("idealtechnologies.co.zw", "google.co.zw", "econet.co.zw", "zimbabwetravel.co.zw", "hararelogistics.com")
        val isTaken = takenList.contains(clean)
        val price = if (clean.endsWith(".co.zw")) 8.0 else if (clean.endsWith(".com")) 16.0 else 18.0
        return Pair(!isTaken, price)
    }
}
