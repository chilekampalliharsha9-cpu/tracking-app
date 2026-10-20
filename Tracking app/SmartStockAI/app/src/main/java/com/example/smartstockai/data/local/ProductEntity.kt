package com.example.smartstockai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
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
    val manufacturingDate: Long? = null,
    val expiryDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt,
    val isActive: Boolean = true
)
