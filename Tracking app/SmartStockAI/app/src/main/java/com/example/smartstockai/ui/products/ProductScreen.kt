package com.example.smartstockai.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstockai.data.local.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen() {
    val viewModel: ProductViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("0") }
    var price by remember { mutableStateOf("0.0") }
    var minStock by remember { mutableStateOf("0") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products") },
                actions = {
                    Button(onClick = { showForm = true }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text("Add")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = {
                    viewModel.setSearchQuery(it)
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text("Search by name, SKU, barcode") },
                singleLine = true
            )

            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = uiState.error ?: "",
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFB91C1C)
                    )
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (showForm) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("New product", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Name") })
                        OutlinedTextField(value = sku, onValueChange = { sku = it }, modifier = Modifier.fillMaxWidth(), label = { Text("SKU") })
                        OutlinedTextField(value = barcode, onValueChange = { barcode = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Barcode") })
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = quantity, onValueChange = { quantity = it }, modifier = Modifier.weight(1f), label = { Text("Qty") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            OutlinedTextField(value = price, onValueChange = { price = it }, modifier = Modifier.weight(1f), label = { Text("Price") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                        }
                        OutlinedTextField(value = minStock, onValueChange = { minStock = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Min stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    val newProduct = ProductEntity(
                                        name = name.trim(),
                                        sku = sku.trim(),
                                        barcode = barcode.trim(),
                                        availableQuantity = quantity.toIntOrNull() ?: 0,
                                        sellingPrice = price.toDoubleOrNull() ?: 0.0,
                                        minimumStock = minStock.toIntOrNull() ?: 0,
                                        createdAt = System.currentTimeMillis(),
                                        updatedAt = System.currentTimeMillis()
                                    )
                                    if (newProduct.name.isBlank() || newProduct.sku.isBlank()) {
                                        viewModel.loadProducts()
                                    } else {
                                        viewModel.addProduct(newProduct)
                                    }
                                    name = ""
                                    sku = ""
                                    barcode = ""
                                    quantity = "0"
                                    price = "0.0"
                                    minStock = "0"
                                    showForm = false
                                }
                            ) {
                                Text("Save")
                            }
                            Button(onClick = { showForm = false }) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }

            if (uiState.products.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No products added yet.", color = Color(0xFF475569), fontSize = 16.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.products) { product ->
                        ProductRow(product = product, onArchive = { viewModel.archiveProduct(product.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun ProductRow(product: ProductEntity, onArchive: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("SKU: ${product.sku}")
                Text("Qty: ${product.availableQuantity} | Min: ${product.minimumStock}")
                Text("Barcode: ${product.barcode.ifBlank { "N/A" }}")
            }
            Icon(
                imageVector = Icons.Filled.Archive,
                contentDescription = "Archive",
                tint = Color(0xFFEF4444),
                modifier = Modifier.clickable { onArchive() }
            )
        }
    }
}
