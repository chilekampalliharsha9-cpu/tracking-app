package com.example.smartstockai.ui.stock

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstockai.data.local.ProductEntity
import com.example.smartstockai.data.local.SmartStockDatabase
import com.example.smartstockai.data.local.StockTransactionEntity
import com.example.smartstockai.data.repository.ProductRepository
import com.example.smartstockai.data.repository.StockTransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class StockUiState(
    val products: List<ProductEntity> = emptyList(),
    val transactions: List<StockTransactionEntity> = emptyList(),
    val selectedProductId: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class StockTransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SmartStockDatabase.getInstance(application)
    private val productRepository = ProductRepository(db.productDao())
    private val stockRepository = StockTransactionRepository(db.stockTransactionDao(), db.productDao())

    private val _uiState = MutableStateFlow(StockUiState())
    val uiState: StateFlow<StockUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            productRepository.getAllProducts().collectLatest { products ->
                _uiState.value = _uiState.value.copy(products = products, isLoading = false)
            }
            stockRepository.getAllTransactions().collectLatest { transactions ->
                _uiState.value = _uiState.value.copy(transactions = transactions)
            }
        }
    }

    fun selectProduct(id: Long) {
        _uiState.value = _uiState.value.copy(selectedProductId = id)
    }

    fun recordMovement(productId: Long, type: String, quantity: Int, reason: String) {
        viewModelScope.launch {
            try {
                if (productId <= 0) {
                    _uiState.value = _uiState.value.copy(error = "Select a valid product.")
                    return@launch
                }
                if (quantity <= 0) {
                    _uiState.value = _uiState.value.copy(error = "Quantity must be greater than zero.")
                    return@launch
                }
                stockRepository.recordStockMovement(productId, type, quantity, reason)
                _uiState.value = _uiState.value.copy(
                    successMessage = "$type recorded successfully.",
                    error = null
                )
                loadData()
            } catch (e: IllegalStateException) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Stock validation failed.")
            } catch (e: IllegalArgumentException) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Invalid stock operation.")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Unexpected error: ${e.message}")
            }
        }
    }
}
