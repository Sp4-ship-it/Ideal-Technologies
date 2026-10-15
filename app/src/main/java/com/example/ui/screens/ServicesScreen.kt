package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.BillingType
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.ui.IdealViewModel
import com.example.ui.theme.*
import com.example.ui.util.IntentUtils

@Composable
fun ServicesScreen(
    viewModel: IdealViewModel,
    modifier: Modifier = Modifier
) {
    val services by viewModel.services.collectAsState()
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }
    val context = LocalContext.current

    val displayedServices = remember(services, selectedCategory) {
        if (selectedCategory == null) services else services.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("services_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Header & Interactive Quote Calculator Promo
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IdealNavyDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IdealCyan.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "TRANSPARENT DIRECTORY",
                                style = MaterialTheme.typography.labelSmall,
                                color = IdealCyanBright,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "Zimbabwe & Global",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Services & Standard Pricing",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Zero hidden costs. Order turnkey websites, fast SSD cPanel hosting, official .co.zw domains & corporate branding.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.openQuoteBuilder() },
                        colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("services_open_quote_builder")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Interactive Cost Estimator", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Category Filter Tabs
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All Services") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IdealOrange,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                items(ServiceCategory.entries) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IdealOrange,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        // Services List
        items(displayedServices) { service ->
            ServiceCard(
                service = service,
                onOrderNow = {
                    viewModel.initiatePayment(service.title, service.minPrice)
                },
                onRequestQuote = {
                    viewModel.openQuoteBuilder(service)
                },
                onWhatsAppInquire = {
                    IntentUtils.openWhatsApp(
                        context,
                        "Hello Ideal Technologies, I'd like to inquire about: ${service.title} (${service.priceDescription})."
                    )
                }
            )
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceItem,
    onOrderNow: () -> Unit,
    onRequestQuote: () -> Unit,
    onWhatsAppInquire: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("service_card_${service.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IdealNavyLight.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = service.category.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = IdealNavyLight,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (service.popular) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = IdealOrange.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "MOST POPULAR",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IdealOrange,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Price badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = IdealNavyDark
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = service.priceDescription,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = IdealOrange
                        )
                        Text(
                            text = service.billingType.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Turnaround pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = IdealCyan,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Delivery Time: ${service.turnaroundTime}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(modifier = Modifier.height(10.dp))

            // Feature list
            service.features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRequestQuote,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text("Customize Quote", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = onOrderNow,
                    colors = ButtonDefaults.buttonColors(containerColor = IdealOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Order Now", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                }

                IconButton(
                    onClick = onWhatsAppInquire,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF25D366).copy(alpha = 0.12f))
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "WhatsApp",
                        tint = Color(0xFF25D366),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
