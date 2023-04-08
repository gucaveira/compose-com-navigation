package br.com.alura.panucci.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.alura.panucci.dao.ProductDao
import br.com.alura.panucci.navigation.productIdArgument
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val dao: ProductDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<String?>(productIdArgument, null)
                .filterNotNull()
                .collect { id ->
                    findProductById(id)
                }
        }
    }

    fun findProductById(id: String) {
        _uiState.update { ProductDetailsUiState.Loading }

        val dataState = dao.findById(id)?.let { product ->
            ProductDetailsUiState.Success(product = product)

        } ?: ProductDetailsUiState.Failure

        _uiState.update { dataState }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProductDetailsViewModel(ProductDao(), createSavedStateHandle())
            }
        }
    }
}