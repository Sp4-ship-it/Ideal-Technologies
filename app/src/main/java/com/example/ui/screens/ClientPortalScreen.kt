package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.HostingSubscription
import com.example.data.model.RepairStage
import com.example.data.model.RepairTicket
import com.example.ui.IdealViewModel
import com.example.ui.theme.*
import com.example.ui.util.IntentUtils

enum class PortalSubTab(val title: String) {
    REPAIRS("Hardware Repairs"),
    HOSTING("Hosting & Domains"),
    DOMAIN_CHECKER("Check Domain")
}

@Composable
fun ClientPortalScreen(
    viewModel: IdealViewModel,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf(PortalSubTab.REPAIRS) }
    val activeTicket by viewModel.activeTicket.collectAsState()
    val ticketQuery by viewModel.ticketSearchQuery.collectAsState()
    val hostingList by viewModel.hostingSubscriptions.collectAsState()
    val domainInput by viewModel.domainSearchInput.collectAsState()
    val domainCheckResult by viewModel.domainCheckResult.collectAsState()
    val context = LocalContext.current

    val sampleTickets = listOf("IT-8492", "IT-9104", "IT-7721")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("client_portal_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Sub-Tab Switcher
        item {
            TabRow(
                selectedTabIndex = activeSubTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IdealOrange,
                modifier = Modifier.fillMaxWidth()
            ) {
                PortalSubTab.values().forEach { tab ->
                    Tab(
                        selected = activeSubTab == tab,
                        onClick = { activeSubTab = tab },
                        text = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    )
                }
            }
        }

        when (activeSubTab) {
            PortalSubTab.REPAIRS -> {
                // Search Ticket Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Repair Status Tracker",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Real-time diagnostic bench updates",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = { viewModel.openBookRepairDialog() },
                                colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Book Repair", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = ticketQuery,
                            onValueChange = { viewModel.setTicketSearchQuery(it) },
                            placeholder = { Text("Enter Ticket ID (e.g. IT-8492)") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ticket_search_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Quick sample tickets
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Sample Tickets:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            sampleTickets.forEach { tid ->
                                AssistChip(
                                    onClick = { viewModel.setTicketSearchQuery(tid) },
                                    label = { Text(tid, fontSize = 11.sp) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }

                // Active Ticket Card
                if (activeTicket != null) {
                    item {
                        RepairTicketDetailsView(
                            ticket = activeTicket!!,
                            onWhatsAppChat = {
                                IntentUtils.openWhatsApp(
                                    context,
                                    "Hello Ideal Tech Lab, I am inquiring regarding ticket ${activeTicket!!.ticketId} (${activeTicket!!.deviceModel})."
                                )
                            },
                            onPayInvoice = {
                                viewModel.initiatePayment("Repair Invoice: ${activeTicket!!.ticketId} (${activeTicket!!.deviceModel})", activeTicket!!.totalCostUsd)
                            }
                        )
                    }
                } else {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Build,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No repair ticket found for \"$ticketQuery\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Try IT-8492, IT-9104, or book a new diagnostic intake.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            PortalSubTab.HOSTING -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Hosting & Domain Subscriptions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Monitor expiration dates, SSL health & renewal status",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(hostingList) { sub ->
                    HostingSubscriptionCard(
                        subscription = sub,
                        onRenew = {
                            viewModel.initiatePayment("Renewal: ${sub.domainName} (${sub.planName})", sub.annualFeeUsd)
                        }
                    )
                }
            }

            PortalSubTab.DOMAIN_CHECKER -> {
                item {
                    DomainCheckerSection(
                        domainInput = domainInput,
                        onInputChange = { viewModel.setDomainSearchInput(it) },
                        onCheck = { viewModel.checkDomain() },
                        result = domainCheckResult,
                        onRegister = { domain, price ->
                            viewModel.initiatePayment("Domain Registration: $domain", price)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RepairTicketDetailsView(
    ticket: RepairTicket,
    onWhatsAppChat: () -> Unit,
    onPayInvoice: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = IdealNavyDark
                ) {
                    Text(
                        text = ticket.ticketId,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = IdealOrange,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (ticket.currentStage) {
                        RepairStage.READY_FOR_PICKUP -> StatusSuccess.copy(alpha = 0.15f)
                        RepairStage.REPAIRING -> IdealOrange.copy(alpha = 0.15f)
                        else -> IdealCyan.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = ticket.currentStage.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (ticket.currentStage) {
                            RepairStage.READY_FOR_PICKUP -> StatusSuccess
                            RepairStage.REPAIRING -> IdealOrange
                            else -> IdealNavyLight
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = ticket.deviceModel,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Client: ${ticket.clientName} • Serial: ${ticket.serialNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Stage Stepper Visualizer
            Text(
                text = "Diagnostic & Repair Progress",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                RepairStage.entries.forEach { stage ->
                    val isPastOrCurrent = stage.stepNumber <= ticket.currentStage.stepNumber
                    val isCurrent = stage == ticket.currentStage

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> IdealOrange
                                        isPastOrCurrent -> StatusSuccess
                                        else -> MaterialTheme.colorScheme.outlineVariant
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isPastOrCurrent && !isCurrent) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            } else {
                                Text(
                                    text = "${stage.stepNumber}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stage.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrent) IdealOrange else MaterialTheme.colorScheme.onSurface
                            )
                            if (isCurrent) {
                                Text(
                                    text = stage.description,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Findings Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Reported Fault",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ticket.reportedIssue,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Lead Technician Findings",
                        style = MaterialTheme.typography.labelSmall,
                        color = IdealNavyLight
                    )
                    Text(
                        text = ticket.technicianFindings,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Lab Location: ${ticket.location}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Cost summary
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = IdealNavyDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ESTIMATED TOTAL (LABOR + PARTS)",
                            style = MaterialTheme.typography.labelSmall,
                            color = IdealCyanBright
                        )
                        Text(
                            text = "$${ticket.totalCostUsd.toInt()} USD",
                            style = MaterialTheme.typography.headlineSmall,
                            color = IdealOrange,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Button(
                        onClick = onPayInvoice,
                        colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Pay Invoice")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onWhatsAppChat,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Chat with Bench Technician on WhatsApp")
            }
        }
    }
}

@Composable
private fun HostingSubscriptionCard(
    subscription: HostingSubscription,
    onRenew: () -> Unit
) {
    val isExpiringSoon = subscription.daysRemaining <= 10

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subscription.domainName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isExpiringSoon) StatusError.copy(alpha = 0.15f) else StatusSuccess.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${subscription.daysRemaining} Days Left",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isExpiringSoon) StatusError else StatusSuccess,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subscription.planName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(14.dp))
                    Text("AutoSSL Active", style = MaterialTheme.typography.labelSmall, color = StatusSuccess)
                }

                Text(
                    text = "Renewal: ${subscription.expirationDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ANNUAL RENEWAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${subscription.annualFeeUsd.toInt()} USD / yr",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = IdealOrange
                    )
                }

                Button(
                    onClick = onRenew,
                    colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Renew Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DomainCheckerSection(
    domainInput: String,
    onInputChange: (String) -> Unit,
    onCheck: () -> Unit,
    result: Pair<Boolean, Double>?,
    onRegister: (String, Double) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Domain Name Search (.co.zw, .com)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Instant WHOIS lookup and registrar allocation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = domainInput,
                    onValueChange = onInputChange,
                    placeholder = { Text("e.g. mybusiness.co.zw") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = onCheck,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IdealOrange)
                ) {
                    Text("Check")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (result != null) {
                val (isAvailable, price) = result
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAvailable) StatusSuccess.copy(alpha = 0.12f) else StatusError.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(
                                    if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (isAvailable) StatusSuccess else StatusError
                                )
                                Text(
                                    text = if (isAvailable) "Domain is Available!" else "Domain is Taken",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAvailable) StatusSuccess else StatusError
                                )
                            }
                            Text(
                                text = "$domainInput ($$price USD / year)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (isAvailable) {
                            Button(
                                onClick = { onRegister(domainInput, price) },
                                colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Register Now")
                            }
                        }
                    }
                }
            }
        }
    }
}
