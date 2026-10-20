package com.example.smartstockai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.smartstockai.ui.products.ProductScreen
import com.example.smartstockai.ui.scanner.ScannerScreen
import com.example.smartstockai.ui.stock.StockMovementScreen
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartStockAIApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartStockAIApp() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color(0xFF0F172A))
    systemUiController.setNavigationBarColor(color = Color(0xFF0F172A))

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF2563EB),
            secondary = Color(0xFF14B8A6),
            background = Color(0xFFF8FAFC),
            surface = Color(0xFFFFFFFF)
        ),
        typography = MaterialTheme.typography,
        content = {
            val tabs = listOf(
                BottomTab("Dashboard", Icons.Filled.BarChart),
                BottomTab("Products", Icons.Filled.Inventory2),
                BottomTab("Scan", Icons.Filled.QrCodeScanner),
                BottomTab("AI Insights", Icons.Filled.Analytics),
                BottomTab("More", Icons.Filled.Settings)
            )

            var selectedTab by remember { mutableIntStateOf(0) }

            Scaffold(
                containerColor = Color(0xFFF8FAFC),
                bottomBar = {
                    BottomAppBar(
                        containerColor = Color(0xFF0F172A),
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            tabs.forEachIndexed { index, tab ->
                                val selected = index == selectedTab
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { selectedTab = index },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.label,
                                            tint = if (selected) Color.White else Color(0xFFCBD5E1)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.label,
                                            fontSize = 11.sp,
                                            color = if (selected) Color.White else Color(0xFFCBD5E1)
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = Color.White,
                                        unselectedIconColor = Color(0xFFCBD5E1),
                                        unselectedTextColor = Color(0xFFCBD5E1),
                                        indicatorColor = Color(0xFF1E293B)
                                    )
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    color = Color(0xFFF8FAFC)
                ) {
                    when (selectedTab) {
                        0 -> DashboardScreen(selectedTab = tabs[0].label)
                        1 -> ProductScreen()
                        2 -> ScannerScreen()
                        3 -> StockMovementScreen()
                        else -> MoreScreen()
                    }
                }
            }
        }
    )
}

private data class BottomTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun DashboardScreen(selectedTab: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SmartStock AI",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Intelligent Mobile Inventory Management",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDBEAFE))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AI", color = Color(0xFF1D4ED8), fontWeight = FontWeight.Bold)
                }
            }

            StatCard(
                label = "Total products",
                value = "245",
                trend = "+12% vs last week",
                accent = Color(0xFF2563EB)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(title = "Inventory value", value = "₹4,82,500", subtitle = "Across 8 locations", accent = Color(0xFF2563EB))
                MetricCard(title = "Low stock", value = "18", subtitle = "Needs attention", accent = Color(0xFFF59E0B))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(title = "Units available", value = "12,460", subtitle = "Healthy visibility", accent = Color(0xFF14B8A6))
                MetricCard(title = "Expiring soon", value = "07", subtitle = "4 batches flagged", accent = Color(0xFFEF4444))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "AI recommendations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text("• Reorder rice packets before the weekend rush.", color = Color(0xFF334155))
                    Text("• 4 expiry-sensitive batches require review.", color = Color(0xFF334155))
                    Text("• Demand forecast indicates +8.6% weekly growth for dairy products.", color = Color(0xFF334155))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Current screen: $selectedTab",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
fun StatCard(label: String, value: String, trend: String, accent: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(accent.copy(alpha = 0.12f))
            .padding(18.dp)
    ) {
        Column {
            Text(text = label, color = Color(0xFF475569), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = Color(0xFF0F172A), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = trend, color = accent, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, subtitle: String, accent: Color) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = title, color = Color(0xFF475569), style = MaterialTheme.typography.labelMedium)
            Text(text = value, color = Color(0xFF0F172A), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = accent, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SmartStockAIPreview() {
    SmartStockAIApp()
}
