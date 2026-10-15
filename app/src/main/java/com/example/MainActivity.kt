package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.IdealViewModel
import com.example.ui.MainTab
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: IdealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()

            MyApplicationTheme(darkTheme = darkModeEnabled) {
                IdealAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun IdealAppRoot(viewModel: IdealViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()

    val showQuoteDialog by viewModel.showQuoteDialog.collectAsState()
    val quoteBuilderState by viewModel.quoteBuilderState.collectAsState()

    val showBookRepairModal by viewModel.showBookRepairModal.collectAsState()
    val selectedHardwareDetail by viewModel.selectedHardwareDetail.collectAsState()
    val paymentModalState by viewModel.paymentModalState.collectAsState()
    val latestReceipt by viewModel.latestReceipt.collectAsState()

    // Handle system back navigation to return to Home tab
    if (currentTab != MainTab.HOME) {
        BackHandler {
            viewModel.selectTab(MainTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            IdealTopBar(
                isDarkMode = darkModeEnabled,
                onToggleDarkMode = { viewModel.toggleDarkMode() }
            )
        },
        bottomBar = {
            IdealBottomNavigation(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.HOME -> HomeScreen(viewModel = viewModel)
                MainTab.SERVICES -> ServicesScreen(viewModel = viewModel)
                MainTab.AI_ASSISTANT -> AIAssistantScreen(viewModel = viewModel)
                MainTab.CLIENT_PORTAL -> ClientPortalScreen(viewModel = viewModel)
                MainTab.CONTACT -> ContactAndCheckoutScreen(viewModel = viewModel)
            }
        }

        // Modals & Dialogs
        if (showQuoteDialog) {
            QuoteBuilderDialog(
                state = quoteBuilderState,
                onDismiss = { viewModel.closeQuoteBuilder() },
                onStateChange = { viewModel.updateQuoteState(it) },
                onProceedToPayment = { title, amount ->
                    viewModel.closeQuoteBuilder()
                    viewModel.initiatePayment(title, amount)
                }
            )
        }

        if (showBookRepairModal) {
            BookRepairDialog(
                onDismiss = { viewModel.closeBookRepairDialog() },
                onSubmit = { name, device, issue, serial, loc ->
                    viewModel.submitNewRepairBooking(name, device, issue, serial, loc)
                }
            )
        }

        if (selectedHardwareDetail != null) {
            HardwareDetailDialog(
                item = selectedHardwareDetail!!,
                onDismiss = { viewModel.openHardwareDetail(null) },
                onOrderNow = { item ->
                    viewModel.initiatePayment(item.name, item.priceUsd)
                }
            )
        }

        if (paymentModalState != null) {
            PaymentDialog(
                state = paymentModalState!!,
                onDismiss = { viewModel.closePaymentModal() },
                onMethodSelect = { viewModel.updatePaymentMethod(it) },
                onPhoneChange = { viewModel.updatePaymentPhone(it) },
                onConfirmPayment = { viewModel.completePayment() }
            )
        }

        if (latestReceipt != null) {
            ReceiptDialog(
                transaction = latestReceipt!!,
                onDismiss = { viewModel.dismissReceipt() }
            )
        }
    }
}
