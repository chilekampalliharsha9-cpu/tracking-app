package com.example.smartstockai.domain

data class Product(
    val id: Long = 0,
    val name: String,
    val sku: String,
    val barcode: String = "",
    val category: String = "General",
    val brand: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val purchasePrice: Double = 0.0,
    val sellingPrice: Double = 0.0,
    val availableQuantity: Int = 0,
    val minimumStock: Int = 0,
    val reorderQuantity: Int = 0,
    val supplier: String = "",
    val storageLocation: String = "Main Shelf",
    val unitOfMeasurement: String = "pcs",
    val batchNumber: String = "",
    val manufacturingDateMillis: Long? = null,
    val expiryDateMillis: Long? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt
)
