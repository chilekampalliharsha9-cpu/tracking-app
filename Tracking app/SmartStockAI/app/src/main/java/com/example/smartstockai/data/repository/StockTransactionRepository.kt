package com.example.smartstockai.data.repository

import com.example.smartstockai.data.local.ProductDao
import com.example.smartstockai.data.local.StockTransactionDao
import com.example.smartstockai.data.local.StockTransactionEntity
import kotlinx.coroutines.flow.Flow

class StockTransactionRepository(
    private val stockTransactionDao: StockTransactionDao,
    private val productDao: ProductDao
) {
    fun getAllTransactions(): Flow<List<StockTransactionEntity>> = stockTransactionDao.getAllTransactions()

    suspend fun recordStockMovement(productId: Long, type: String, quantity: Int, reason: String): Long {
        return stockTransactionDao.recordStockMovement(productId, type, quantity, reason, productDao)
    }
}
