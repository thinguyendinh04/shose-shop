package com.nqmgaming.shoseshop.ui.activities.productDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.product.Product
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun getProductDetail(token: String, id: String) =
        repository.getProductById(token, id)

    fun getProductDetail(token: String, id: String, callback: (Product?) -> Unit) {
        viewModelScope.launch {
            try {
                val productDetail = getProductDetail(token, id)
                callback(productDetail)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error getting product detail: ${e.message}")
                callback(null)
            }
        }
    }
}