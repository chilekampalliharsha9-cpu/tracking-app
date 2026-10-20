package com.example.smartstockai.data.repository

import com.example.smartstockai.data.local.ProductDao
import com.example.smartstockai.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllActiveProducts()

    fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)

    suspend fun getProductById(id: Long): ProductEntity? = productDao.getProductById(id)

    suspend fun findByBarcode(barcode: String): ProductEntity? = productDao.findByBarcode(barcode)

    suspend fun insertProduct(product: ProductEntity): Long = productDao.insert(product)

    suspend fun updateProduct(product: ProductEntity) = productDao.update(product)

    suspend fun archiveProduct(id: Long) = productDao.archiveProduct(id, System.currentTimeMillis())

    suspend fun countProducts(): Int = productDao.countActiveProducts()
}
