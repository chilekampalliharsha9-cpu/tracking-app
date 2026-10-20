package com.example.smartstockai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY updatedAt DESC")
    fun getAllActiveProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isActive = 1 AND (LOWER(name) LIKE '%' || LOWER(:query) || '%' OR LOWER(sku) LIKE '%' || LOWER(:query) || '%' OR LOWER(barcode) LIKE '%' || LOWER(:query) || '%') ORDER BY updatedAt DESC")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    @Query("UPDATE products SET availableQuantity = :quantity, updatedAt = :updatedAt WHERE id = :productId")
    suspend fun updateAvailableQuantity(productId: Long, quantity: Int, updatedAt: Long)

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun findByBarcode(barcode: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity): Long

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("UPDATE products SET isActive = 0, updatedAt = :updatedAt WHERE id = :id")
    suspend fun archiveProduct(id: Long, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM products WHERE isActive = 1")
    suspend fun countActiveProducts(): Int
}
