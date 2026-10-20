package com.example.smartstockai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface StockTransactionDao {
    @Query("SELECT * FROM stock_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<StockTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: StockTransactionEntity): Long

    @Transaction
    suspend fun recordStockMovement(
        productId: Long,
        type: String,
        quantity: Int,
        reason: String,
        productDao: ProductDao
    ): Long {
        val product = productDao.getProductById(productId)
            ?: throw IllegalArgumentException("Product not found")

        val previousQuantity = product.availableQuantity
        val nextQuantity = when (type.uppercase()) {
            "STOCK_IN" -> previousQuantity + quantity
            "STOCK_OUT" -> previousQuantity - quantity
            "ADJUSTMENT" -> previousQuantity + quantity
            else -> throw IllegalArgumentException("Unsupported stock type: $type")
        }

        if (nextQuantity < 0) {
            throw IllegalStateException("Insufficient stock for product ${product.name}")
        }

        productDao.updateAvailableQuantity(productId, nextQuantity, System.currentTimeMillis())

        val transaction = StockTransactionEntity(
            productId = productId,
            type = type.uppercase(),
            quantity = quantity,
            previousQuantity = previousQuantity,
            resultingQuantity = nextQuantity,
            reason = reason,
            timestamp = System.currentTimeMillis()
        )

        return insert(transaction)
    }
}
