package com.example.data.model

data class ServiceItem(
    val id: String,
    val category: ServiceCategory,
    val title: String,
    val priceDescription: String,
    val minPrice: Double,
    val maxPrice: Double?,
    val billingType: BillingType,
    val shortDescription: String,
    val features: List<String>,
    val popular: Boolean = false,
    val turnaroundTime: String = "2-5 Business Days"
)

enum class ServiceCategory(val displayName: String) {
    WEB_AND_SOFTWARE("Web & Software"),
    HOSTING_AND_DOMAINS("Domains & Hosting"),
    BRANDING_AND_MARKETING("Branding & Design"),
    IT_SUPPORT_AND_REPAIRS("Hardware & Support")
}

enum class BillingType(val label: String) {
    ONE_TIME("One-time"),
    MONTHLY("/ month"),
    ANNUAL("/ year"),
    SETUP_FEE("Setup fee")
}

data class HardwareItem(
    val id: String,
    val name: String,
    val category: HardwareCategory,
    val priceUsd: Double,
    val originalPriceUsd: Double? = null,
    val specs: List<String>,
    val condition: String = "Brand New / Certified",
    val inStock: Boolean = true,
    val stockCount: Int = 5,
    val warrantyMonths: Int = 12,
    val isFeatured: Boolean = false
)

enum class HardwareCategory(val displayName: String) {
    ALL("All Products"),
    LAPTOPS("Laptops & PCs"),
    NETWORKING("Networking Gear"),
    ACCESSORIES("Accessories"),
    STORAGE("Storage & Upgrades")
}

enum class RepairStage(val stepNumber: Int, val title: String, val description: String) {
    RECEIVED(1, "Device Received", "Logged at technical intake counter"),
    DIAGNOSING(2, "Hardware Diagnostics", "Component level testing in progress"),
    PARTS_SOURCING(3, "Parts Allocation", "Genuine replacement parts confirmed"),
    REPAIRING(4, "Active Bench Repair", "Soldering / replacement / calibration"),
    QUALITY_CHECK(5, "Quality Assurance", "Stress testing and OS verification"),
    READY_FOR_PICKUP(6, "Ready for Collection", "Complete & ready for pickup/delivery")
}

data class RepairTicket(
    val ticketId: String,
    val clientName: String,
    val deviceModel: String,
    val serialNumber: String,
    val intakeDate: String,
    val estimatedCompletion: String,
    val currentStage: RepairStage,
    val reportedIssue: String,
    val technicianFindings: String,
    val laborCostUsd: Double,
    val partsCostUsd: Double,
    val technicianName: String = "Tinashe M. (Lead Hardware Engineer)",
    val location: String = "Harare Main Lab, Joina City"
) {
    val totalCostUsd: Double get() = laborCostUsd + partsCostUsd
}

data class HostingSubscription(
    val domainName: String,
    val planName: String,
    val registrationDate: String,
    val expirationDate: String,
    val daysRemaining: Int,
    val annualFeeUsd: Double,
    val sslActive: Boolean = true,
    val storageUsageGb: Double = 3.2,
    val storageTotalGb: Double = 20.0,
    val primaryNameserver: String = "ns1.idealtechnologies.co.zw"
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val serviceQuoteEstimate: Double? = null,
    val suggestedAction: String? = null
)

data class PaymentTransaction(
    val referenceId: String,
    val itemDescription: String,
    val amountUsd: Double,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val timestamp: String,
    val payerPhone: String = "",
    val notes: String = ""
)

enum class PaymentMethod(val displayName: String, val dialCode: String, val instructions: String) {
    ECOCASH_USD("EcoCash USD", "*151#", "Merchant Code: 284910 (Ideal Technologies Pvt Ltd)"),
    INNBUCKS("InnBucks USD", "*569#", "Pay into Till/Account #0781834073 (Ideal Technologies)"),
    ONEMONEY("OneMoney", "*111#", "Merchant Bill: 781834 (Ideal Tech)"),
    BANK_NOSTRO("Stanbic/CBZ USD Nostro", "N/A", "A/C: 9140003847291 (Branch: Harare Nelson Mandela)"),
    VISA_MASTERCARD("Visa / Mastercard", "N/A", "Instant online processing (Stripe / Paynow gateway)")
}

enum class PaymentStatus {
    PENDING,
    CONFIRMED,
    FAILED
}
