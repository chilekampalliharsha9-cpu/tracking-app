package com.example.smartstockai.ui.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstockai.data.local.ProductEntity
import com.example.smartstockai.data.local.SmartStockDatabase
import com.example.smartstockai.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ProductUiState(
    val products: List<ProductEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductRepository = ProductRepository(
        SmartStockDatabase.getInstance(application).productDao()
    )

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isBlank()) {
            loadProducts()
        } else {
            viewModelScope.launch {
                repository.searchProducts(query).collectLatest { products ->
                    _uiState.value = _uiState.value.copy(products = products, isLoading = false)
                }
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getAllProducts().collectLatest { products ->
                _uiState.value = _uiState.value.copy(products = products, isLoading = false)
            }
        }
    }

    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            val existing = repository.findByBarcode(product.barcode)
            if (product.barcode.isNotBlank() && existing != null && existing.id != product.id) {
                _uiState.value = _uiState.value.copy(error = "Duplicate barcode already exists.")
                return@launch
            }

            repository.insertProduct(product)
            loadProducts()
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.updateProduct(product)
            loadProducts()
        }
    }

    fun archiveProduct(productId: Long) {
        viewModelScope.launch {
            repository.archiveProduct(productId)
            loadProducts()
        }
    }
}
