package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiService
import com.example.data.model.*
import com.example.data.repository.IdealRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class MainTab(val title: String) {
    HOME("Home"),
    SERVICES("Services & Pricing"),
    AI_ASSISTANT("Ideal Inside AI"),
    CLIENT_PORTAL("Client Portal"),
    CONTACT("Contact & Pay")
}

data class QuoteBuilderState(
    val selectedService: ServiceItem? = null,
    val includeCozwDomain: Boolean = true,
    val includeStarterHosting: Boolean = true,
    val includeCustomEmail: Boolean = false,
    val includeSpeedOptimization: Boolean = true,
    val includeBrandIdentity: Boolean = false,
    val notes: String = ""
) {
    val estimatedTotal: Double
        get() {
            var sum = selectedService?.minPrice ?: 60.0
            if (includeCozwDomain) sum += 8.0
            if (includeStarterHosting) sum += 35.0
            if (includeCustomEmail) sum += 20.0
            if (includeSpeedOptimization) sum += 25.0
            if (includeBrandIdentity) sum += 65.0
            return sum
        }
}

data class PaymentDialogState(
    val itemTitle: String,
    val amountUsd: Double,
    val clientPhone: String = "",
    val method: PaymentMethod = PaymentMethod.ECOCASH_USD,
    val notes: String = ""
)

class IdealViewModel(
    private val repository: IdealRepository = IdealRepository(),
    private val geminiService: GeminiService = GeminiService()
) : ViewModel() {

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    val services = repository.services
    val hardwareItems = repository.hardwareItems
    val repairTickets = repository.repairTickets
    val hostingSubscriptions = repository.hostingSubscriptions
    val transactions = repository.transactions

    // Hardware filters
    private val _selectedHardwareCategory = MutableStateFlow(HardwareCategory.ALL)
    val selectedHardwareCategory: StateFlow<HardwareCategory> = _selectedHardwareCategory.asStateFlow()

    private val _hardwareSearchQuery = MutableStateFlow("")
    val hardwareSearchQuery: StateFlow<String> = _hardwareSearchQuery.asStateFlow()

    val filteredHardware = combine(hardwareItems, selectedHardwareCategory, _hardwareSearchQuery) { items, cat, query ->
        items.filter { item ->
            val matchCat = cat == HardwareCategory.ALL || item.category == cat
            val matchQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.specs.any { it.contains(query, ignoreCase = true) }
            matchCat && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Repair ticket tracking
    private val _ticketSearchQuery = MutableStateFlow("IT-8492")
    val ticketSearchQuery: StateFlow<String> = _ticketSearchQuery.asStateFlow()

    private val _activeTicket = MutableStateFlow<RepairTicket?>(null)
    val activeTicket: StateFlow<RepairTicket?> = _activeTicket.asStateFlow()

    // Domain availability checker
    private val _domainSearchInput = MutableStateFlow("")
    val domainSearchInput: StateFlow<String> = _domainSearchInput.asStateFlow()

    private val _domainCheckResult = MutableStateFlow<Pair<Boolean, Double>?>(null)
    val domainCheckResult: StateFlow<Pair<Boolean, Double>?> = _domainCheckResult.asStateFlow()

    // AI Assistant Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                isUser = false,
                text = "Welcome to Ideal Technologies! I'm Ideal Inside, your 24/7 AI technology consultant. Ask me about custom web development, .co.zw domains, high-speed hosting, hardware diagnostics, or request an instant project estimate."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAILoading = MutableStateFlow(false)
    val isAILoading: StateFlow<Boolean> = _isAILoading.asStateFlow()

    // Interactive Dialogs & Builders
    private val _quoteBuilderState = MutableStateFlow(QuoteBuilderState())
    val quoteBuilderState: StateFlow<QuoteBuilderState> = _quoteBuilderState.asStateFlow()

    private val _showQuoteDialog = MutableStateFlow(false)
    val showQuoteDialog: StateFlow<Boolean> = _showQuoteDialog.asStateFlow()

    private val _selectedHardwareDetail = MutableStateFlow<HardwareItem?>(null)
    val selectedHardwareDetail: StateFlow<HardwareItem?> = _selectedHardwareDetail.asStateFlow()

    private val _showBookRepairModal = MutableStateFlow(false)
    val showBookRepairModal: StateFlow<Boolean> = _showBookRepairModal.asStateFlow()

    private val _paymentModalState = MutableStateFlow<PaymentDialogState?>(null)
    val paymentModalState: StateFlow<PaymentDialogState?> = _paymentModalState.asStateFlow()

    private val _latestReceipt = MutableStateFlow<PaymentTransaction?>(null)
    val latestReceipt: StateFlow<PaymentTransaction?> = _latestReceipt.asStateFlow()

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled.asStateFlow()

    init {
        searchTicket("IT-8492")
    }

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun toggleDarkMode() {
        _darkModeEnabled.value = !_darkModeEnabled.value
    }

    fun setHardwareCategory(cat: HardwareCategory) {
        _selectedHardwareCategory.value = cat
    }

    fun setHardwareSearchQuery(query: String) {
        _hardwareSearchQuery.value = query
    }

    fun openHardwareDetail(item: HardwareItem?) {
        _selectedHardwareDetail.value = item
    }

    fun setTicketSearchQuery(query: String) {
        _ticketSearchQuery.value = query
        searchTicket(query)
    }

    fun searchTicket(query: String) {
        _activeTicket.value = repository.findTicket(query)
    }

    fun setDomainSearchInput(input: String) {
        _domainSearchInput.value = input
    }

    fun checkDomain() {
        val domain = _domainSearchInput.value.trim()
        if (domain.isNotBlank()) {
            _domainCheckResult.value = repository.checkDomainAvailability(domain)
        }
    }

    fun openQuoteBuilder(service: ServiceItem? = null) {
        _quoteBuilderState.value = QuoteBuilderState(selectedService = service ?: services.value.firstOrNull())
        _showQuoteDialog.value = true
    }

    fun closeQuoteBuilder() {
        _showQuoteDialog.value = false
    }

    fun updateQuoteState(update: QuoteBuilderState.() -> QuoteBuilderState) {
        _quoteBuilderState.value = _quoteBuilderState.value.update()
    }

    fun openBookRepairDialog() {
        _showBookRepairModal.value = true
    }

    fun closeBookRepairDialog() {
        _showBookRepairModal.value = false
    }

    fun submitNewRepairBooking(
        clientName: String,
        deviceModel: String,
        issueDescription: String,
        serialNumber: String,
        pickupLocation: String
    ) {
        val randId = "IT-" + (1000..9999).random()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val today = dateFormat.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 3)
        val estDate = dateFormat.format(cal.time)

        val newTicket = RepairTicket(
            ticketId = randId,
            clientName = clientName.ifBlank { "Client" },
            deviceModel = deviceModel.ifBlank { "Laptop Device" },
            serialNumber = serialNumber.ifBlank { "SN-${(100000..999999).random()}" },
            intakeDate = today,
            estimatedCompletion = estDate,
            currentStage = RepairStage.RECEIVED,
            reportedIssue = issueDescription.ifBlank { "General hardware diagnosis requested" },
            technicianFindings = "Device logged into technical queue. Intake inspection scheduled within 2 hours.",
            laborCostUsd = 30.0,
            partsCostUsd = 0.0,
            location = if (pickupLocation.isNotBlank()) pickupLocation else "Harare Lab - Joina City 4th Floor"
        )
        repository.addRepairTicket(newTicket)
        _activeTicket.value = newTicket
        _ticketSearchQuery.value = randId
        closeBookRepairDialog()
        selectTab(MainTab.CLIENT_PORTAL)
    }

    fun initiatePayment(itemTitle: String, amountUsd: Double, initialMethod: PaymentMethod = PaymentMethod.ECOCASH_USD) {
        _paymentModalState.value = PaymentDialogState(
            itemTitle = itemTitle,
            amountUsd = amountUsd,
            method = initialMethod
        )
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        _paymentModalState.value = _paymentModalState.value?.copy(method = method)
    }

    fun updatePaymentPhone(phone: String) {
        _paymentModalState.value = _paymentModalState.value?.copy(clientPhone = phone)
    }

    fun closePaymentModal() {
        _paymentModalState.value = null
    }

    fun completePayment() {
        val state = _paymentModalState.value ?: return
        val randRef = "IDL-" + (100000..999999).random()
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val timestamp = dateFormat.format(Date())

        val tx = PaymentTransaction(
            referenceId = randRef,
            itemDescription = state.itemTitle,
            amountUsd = state.amountUsd,
            method = state.method,
            status = PaymentStatus.CONFIRMED,
            timestamp = timestamp,
            payerPhone = state.clientPhone,
            notes = "Payment processed successfully via Ideal Gateway."
        )
        repository.addTransaction(tx)
        _latestReceipt.value = tx
        _paymentModalState.value = null
    }

    fun dismissReceipt() {
        _latestReceipt.value = null
    }

    fun sendAIMessage(prompt: String) {
        if (prompt.isBlank() || _isAILoading.value) return

        val userMsg = ChatMessage(isUser = true, text = prompt)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAILoading.value = true

        viewModelScope.launch {
            val history = _chatMessages.value.map { Pair(it.text, it.isUser) }
            val responseText = geminiService.sendMessage(prompt, history)

            val aiMsg = ChatMessage(isUser = false, text = responseText)
            _chatMessages.value = _chatMessages.value + aiMsg
            _isAILoading.value = false
        }
    }

    fun clearAIChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                isUser = false,
                text = "Chat history cleared. How can Ideal Inside assist you with web engineering, hosting, or hardware today?"
            )
        )
    }
}
