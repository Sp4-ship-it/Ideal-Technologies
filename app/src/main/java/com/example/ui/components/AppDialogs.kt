package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.IdealViewModel
import com.example.ui.PaymentDialogState
import com.example.ui.QuoteBuilderState
import com.example.ui.theme.*
import com.example.ui.util.IntentUtils

@Composable
fun QuoteBuilderDialog(
    state: QuoteBuilderState,
    onDismiss: () -> Unit,
    onStateChange: (QuoteBuilderState.() -> QuoteBuilderState) -> Unit,
    onProceedToPayment: (String, Double) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("quote_builder_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Interactive Quote Builder",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tailor your project specifications",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Base Service selected
                Text(
                    text = "Base Service Tier",
                    style = MaterialTheme.typography.labelLarge,
                    color = IdealNavyLight
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.selectedService?.title ?: "Web Development Tier",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.selectedService?.priceDescription ?: "$60 - $100",
                                style = MaterialTheme.typography.bodyMedium,
                                color = IdealOrange
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Recommended Addons & Infrastructure",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Checkbox 1: .co.zw Domain
                QuoteAddonCheckbox(
                    title = ".co.zw Local Domain Registration",
                    subtitle = "1 Year registration & DNS management",
                    price = "+$8.00",
                    checked = state.includeCozwDomain,
                    onCheckedChange = { onStateChange { copy(includeCozwDomain = it) } }
                )

                // Checkbox 2: Starter Web Hosting
                QuoteAddonCheckbox(
                    title = "Starter High-Speed SSD Hosting",
                    subtitle = "cPanel, free AutoSSL, 5 emails (1 Year)",
                    price = "+$35.00",
                    checked = state.includeStarterHosting,
                    onCheckedChange = { onStateChange { copy(includeStarterHosting = it) } }
                )

                // Checkbox 3: Custom Corporate Email Setup
                QuoteAddonCheckbox(
                    title = "Custom Domain Email Configuration",
                    subtitle = "Zoho Mail / Google Workspace with SPF & DKIM",
                    price = "+$20.00",
                    checked = state.includeCustomEmail,
                    onCheckedChange = { onStateChange { copy(includeCustomEmail = it) } }
                )

                // Checkbox 4: Performance & Speed Optimization
                QuoteAddonCheckbox(
                    title = "Speed & Mobile Core Web Vitals Optimization",
                    subtitle = "Caching, image compression & CDN configuration",
                    price = "+$25.00",
                    checked = state.includeSpeedOptimization,
                    onCheckedChange = { onStateChange { copy(includeSpeedOptimization = it) } }
                )

                // Checkbox 5: Full Brand Identity Package
                QuoteAddonCheckbox(
                    title = "Full Brand Identity Package Bundle",
                    subtitle = "3 Logo concepts, letterhead, business card artwork",
                    price = "+$65.00",
                    checked = state.includeBrandIdentity,
                    onCheckedChange = { onStateChange { copy(includeBrandIdentity = it) } }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Total Estimate Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = IdealNavyDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "ESTIMATED TOTAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = IdealCyanBright,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${String.format(java.util.Locale.US, "%.2f", state.estimatedTotal)} USD",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Button(
                            onClick = {
                                val quoteSummary = "Quote: ${state.selectedService?.title ?: "Custom Project"} - Total: $${state.estimatedTotal} USD"
                                onProceedToPayment(quoteSummary, state.estimatedTotal)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("quote_checkout_button")
                        ) {
                            Text("Order Now", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        val message = "Hello Ideal Technologies! I configured a project quote on the Ideal App:\n" +
                                "• Service: ${state.selectedService?.title ?: "Web Development"}\n" +
                                "• Domain (.co.zw): ${if (state.includeCozwDomain) "Included" else "No"}\n" +
                                "• SSD Hosting: ${if (state.includeStarterHosting) "Included" else "No"}\n" +
                                "• Custom Emails: ${if (state.includeCustomEmail) "Included" else "No"}\n" +
                                "• Total Estimate: $${state.estimatedTotal} USD\n" +
                                "Please advise on project kickoff."
                        IntentUtils.openWhatsApp(context, message)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quote_whatsapp_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send Estimate to WhatsApp")
                }
            }
        }
    }
}

@Composable
private fun QuoteAddonCheckbox(
    title: String,
    subtitle: String,
    price: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = IdealOrange)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = price,
            style = MaterialTheme.typography.labelLarge,
            color = IdealOrange,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRepairDialog(
    onDismiss: () -> Unit,
    onSubmit: (clientName: String, deviceModel: String, issueDescription: String, serialNumber: String, pickupLocation: String) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var deviceModel by remember { mutableStateOf("") }
    var issueDescription by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("Harare Lab - Joina City 4th Floor") }

    val locations = listOf(
        "Harare Lab - Joina City 4th Floor",
        "Chinhoyi Lab - Magamba Way",
        "Nationwide Courier Pickup (Harare / Bulawayo / Gweru / Mutare)"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("book_repair_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Book Device Repair",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Fast 24-48h hardware turnaround",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Your Full Name") },
                    placeholder = { Text("e.g. Farai Chitepo") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("repair_input_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = deviceModel,
                    onValueChange = { deviceModel = it },
                    label = { Text("Device Brand & Model") },
                    placeholder = { Text("e.g. HP EliteBook 840 G6, MacBook Air M1") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("repair_input_device")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = issueDescription,
                    onValueChange = { issueDescription = it },
                    label = { Text("Reported Fault / Problem") },
                    placeholder = { Text("e.g. Broken screen, won't power on, liquid spill") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("repair_input_issue")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = serialNumber,
                    onValueChange = { serialNumber = it },
                    label = { Text("Serial Number / Asset Tag (Optional)") },
                    placeholder = { Text("e.g. 5CD14892KL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Intake Location / Pickup Option",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                locations.forEach { loc ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedLocation = loc }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLocation == loc,
                            onClick = { selectedLocation = loc },
                            colors = RadioButtonDefaults.colors(selectedColor = IdealOrange)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(loc, style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSubmit(clientName, deviceModel, issueDescription, serialNumber, selectedLocation)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("repair_submit_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Diagnostic Ticket", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PaymentDialog(
    state: PaymentDialogState,
    onDismiss: () -> Unit,
    onMethodSelect: (PaymentMethod) -> Unit,
    onPhoneChange: (String) -> Unit,
    onConfirmPayment: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("payment_modal_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Payment Gateway",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Secure local & international processing",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = IdealNavyDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ITEM / SERVICE",
                            style = MaterialTheme.typography.labelSmall,
                            color = IdealCyanBright
                        )
                        Text(
                            text = state.itemTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "AMOUNT DUE",
                            style = MaterialTheme.typography.labelSmall,
                            color = IdealCyanBright
                        )
                        Text(
                            text = "$${String.format(java.util.Locale.US, "%.2f", state.amountUsd)} USD",
                            style = MaterialTheme.typography.headlineMedium,
                            color = IdealOrange,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Payment Channel",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                PaymentMethod.entries.forEach { method ->
                    val isSelected = state.method == method
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) IdealOrange.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, IdealOrange) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onMethodSelect(method) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onMethodSelect(method) },
                                colors = RadioButtonDefaults.colors(selectedColor = IdealOrange)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = method.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = method.instructions,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (state.method == PaymentMethod.ECOCASH_USD || state.method == PaymentMethod.INNBUCKS || state.method == PaymentMethod.ONEMONEY) {
                    OutlinedTextField(
                        value = state.clientPhone,
                        onValueChange = onPhoneChange,
                        label = { Text("Payer Mobile Number (e.g. 078XXXXXXX)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("payment_input_phone")
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = onConfirmPayment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("payment_confirm_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm & Authorize Payment", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReceiptDialog(
    transaction: PaymentTransaction,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("receipt_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(StatusSuccess.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = StatusSuccess,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Payment Confirmed",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Ideal Technologies Digital Receipt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ReceiptRow("Reference", transaction.referenceId)
                        ReceiptRow("Item", transaction.itemDescription)
                        ReceiptRow("Method", transaction.method.displayName)
                        ReceiptRow("Timestamp", transaction.timestamp)
                        ReceiptRow("Status", "SUCCESS (200 OK)", isSuccess = true)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        ReceiptRow("Total Paid", "$${String.format(java.util.Locale.US, "%.2f", transaction.amountUsd)} USD", isBold = true)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val slip = "IDEAL TECHNOLOGIES RECEIPT\n" +
                                "Ref: ${transaction.referenceId}\n" +
                                "Item: ${transaction.itemDescription}\n" +
                                "Amount: $${transaction.amountUsd} USD\n" +
                                "Method: ${transaction.method.displayName}\n" +
                                "Date: ${transaction.timestamp}\n" +
                                "Verification: VERIFIED & CONFIRMED"
                        IntentUtils.openWhatsApp(context, slip)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Official Slip to WhatsApp")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Close Receipt")
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, isSuccess: Boolean = false, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = if (isSuccess) StatusSuccess else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun HardwareDetailDialog(
    item: HardwareItem,
    onDismiss: () -> Unit,
    onOrderNow: (HardwareItem) -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("hardware_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = IdealOrange.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = item.condition,
                            style = MaterialTheme.typography.labelSmall,
                            color = IdealOrange,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$${item.priceUsd.toInt()} USD",
                        style = MaterialTheme.typography.headlineMedium,
                        color = IdealOrange,
                        fontWeight = FontWeight.Black
                    )
                    if (item.originalPriceUsd != null) {
                        Text(
                            text = "$${item.originalPriceUsd.toInt()}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Hardware Specifications",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                item.specs.forEach { spec ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = IdealCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(spec, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Warranty", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${item.warrantyMonths} Months", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Inventory", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${item.stockCount} Available", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onOrderNow(item)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("hardware_buy_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Instant Purchase / Checkout", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        IntentUtils.openWhatsApp(context, "Hello Ideal Tech, I am interested in purchasing: ${item.name} for $${item.priceUsd} USD.")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Inquire / Reserve via WhatsApp")
                }
            }
        }
    }
}
