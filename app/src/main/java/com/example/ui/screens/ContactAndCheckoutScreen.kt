package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentMethod
import com.example.data.model.PaymentTransaction
import com.example.ui.IdealViewModel
import com.example.ui.theme.*
import com.example.ui.util.IntentUtils

@Composable
fun ContactAndCheckoutScreen(
    viewModel: IdealViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val transactions by viewModel.transactions.collectAsState()

    var customPayItem by remember { mutableStateOf("Custom Invoice / Service Deposit") }
    var customPayAmount by remember { mutableStateOf("50.00") }
    var inquiryName by remember { mutableStateOf("") }
    var inquiryEmailOrPhone by remember { mutableStateOf("") }
    var inquiryMessage by remember { mutableStateOf("") }
    var formSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("contact_and_checkout_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Direct Action Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IdealNavyDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "DIRECT CONNECT & SUPPORT",
                        style = MaterialTheme.typography.labelSmall,
                        color = IdealCyanBright,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Talk to Our Engineers",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Direct assistance for enterprise inquiries, emergency hosting support, or quotation requests.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { IntentUtils.openWhatsApp(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("contact_whatsapp_btn")
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "WhatsApp (+263 78 183 4073)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = { IntentUtils.dialPhone(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("contact_call_btn")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Directly", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Office Locations Directory
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Office & Technical Hubs Directory",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Walk-in diagnostic centers and remote delivery nationwide",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                LocationCard(
                    city = "Harare (Headquarters & Innovation Hub)",
                    address = "4th Floor, Joina City CBD Innovation Hub, Harare",
                    phone = "+263 78 183 4073",
                    hours = "Mon - Fri: 8:00 AM - 5:30 PM | Sat: 8:30 AM - 1:00 PM"
                )

                Spacer(modifier = Modifier.height(8.dp))

                LocationCard(
                    city = "Chinhoyi (Service & Hardware Lab)",
                    address = "Magamba Way Commercial Arcade, Chinhoyi",
                    phone = "+263 78 183 4073",
                    hours = "Mon - Fri: 8:00 AM - 5:00 PM | Sat: 8:30 AM - 1:00 PM"
                )

                Spacer(modifier = Modifier.height(8.dp))

                LocationCard(
                    city = "Nationwide Delivery & Remote Cloud Support",
                    address = "Secure courier drop-off/pickup across Bulawayo, Gweru, Mutare, Masvingo",
                    phone = "24/7 Server Network Operations Center",
                    hours = "Online Always: idealtechnologies.co.zw"
                )
            }
        }

        // Payment Gateway Simulator Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Payment Gateway Hub",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Process hosting renewals, repair balances, or custom invoices instantly",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Quick Invoice Payment",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customPayItem,
                            onValueChange = { customPayItem = it },
                            label = { Text("Service / Invoice Reference") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customPayAmount,
                            onValueChange = { customPayAmount = it },
                            label = { Text("Amount (USD)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val amount = customPayAmount.toDoubleOrNull() ?: 50.0
                                viewModel.initiatePayment(customPayItem, amount)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("quick_pay_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Launch Payment Gateway ($$customPayAmount USD)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Transactions History
        if (transactions.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Recent Verified Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(transactions) { tx ->
                TransactionCard(tx)
            }
        }

        // Direct Inquiry Message Form
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Send Direct Message to Sales & Tech",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (formSubmitted) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StatusSuccess.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Thank you! Your message has been routed to our Harare support desk.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StatusSuccess
                                )
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = inquiryName,
                            onValueChange = { inquiryName = it },
                            label = { Text("Your Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = inquiryEmailOrPhone,
                            onValueChange = { inquiryEmailOrPhone = it },
                            label = { Text("Email or Phone Number") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = inquiryMessage,
                            onValueChange = { inquiryMessage = it },
                            label = { Text("How can we assist you?") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (inquiryName.isNotBlank() && inquiryMessage.isNotBlank()) {
                                    formSubmitted = true
                                    IntentUtils.sendEmail(
                                        context,
                                        "Inquiry from $inquiryName",
                                        "Name: $inquiryName\nContact: $inquiryEmailOrPhone\n\nMessage:\n$inquiryMessage"
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IdealNavyDark),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Submit Inquiry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationCard(
    city: String,
    address: String,
    phone: String,
    hours: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = IdealOrange, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = city, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = IdealCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = hours, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun TransactionCard(tx: PaymentTransaction) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = tx.itemDescription, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = "Ref: ${tx.referenceId} • ${tx.method.displayName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format(java.util.Locale.US, "%.2f", tx.amountUsd)} USD",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Black,
                    color = IdealOrange
                )
                Text(
                    text = "CONFIRMED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = StatusSuccess
                )
            }
        }
    }
}
