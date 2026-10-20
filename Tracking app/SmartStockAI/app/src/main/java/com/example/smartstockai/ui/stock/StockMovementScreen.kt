package com.example.smartstockai.ui.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstockai.data.local.ProductEntity
import com.example.smartstockai.data.local.StockTransactionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockMovementScreen() {
    val viewModel: StockTransactionViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var selectedProductId by remember { mutableStateOf<Long?>(null) }
    var quantity by remember { mutableStateOf("1") }
    var reason by remember { mutableStateOf("Stock adjustment") }
    var selectedType by remember { mutableStateOf("STOCK_IN") }
    var expanded by remember { mutableStateOf(false) }

    val productOptions = uiState.products
    val selectedProduct = productOptions.find { it.id == selectedProductId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Stock movements", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.error != null) {
                    Text(uiState.error ?: "", color = Color(0xFFB91C1C))
                }
                if (uiState.successMessage != null) {
                    Text(uiState.successMessage ?: "", color = Color(0xFF166534))
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedProduct?.name ?: "Select product",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        label = { Text("Product") }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        productOptions.forEach { product ->
                            DropdownMenuItem(
                                text = { Text(product.name) },
                                onClick = {
                                    selectedProductId = product.id
                                    viewModel.selectProduct(product.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = { selectedType = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Type") }
                    )
                }

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Reason") }
                )

                Button(
                    onClick = {
                        val parsedQuantity = quantity.toIntOrNull() ?: 0
                        val safeProductId = selectedProductId ?: 0L
                        if (safeProductId > 0) {
                            viewModel.recordMovement(safeProductId, selectedType, parsedQuantity, reason)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply stock movement")
                }
            }
        }

        Text("Transaction history", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        if (uiState.transactions.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No stock movements yet.")
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.transactions) { tx ->
                    TransactionCard(tx, uiState.products)
                }
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: StockTransactionEntity, products: List<ProductEntity>) {
    val product = products.find { it.id == transaction.productId }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(transaction.type, fontWeight = FontWeight.Bold)
                Text(formatDate(transaction.timestamp), color = Color(0xFF475569))
            }
            Text(product?.name ?: "Unknown product")
            Text("Qty: ${transaction.quantity} | Previous: ${transaction.previousQuantity} | Result: ${transaction.resultingQuantity}")
            Text("Reason: ${transaction.reason.ifBlank { "Not specified" }}")
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
