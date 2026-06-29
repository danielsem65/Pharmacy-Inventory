package com.pharmacy.inventory.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.inventory.PharmacyApp
import com.pharmacy.inventory.data.model.Medicine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as PharmacyApp).repository

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _lastUpdated = MutableStateFlow(getLastUpdatedPref())
    val lastUpdated: StateFlow<String> = _lastUpdated.asStateFlow()

    private val _itemCount = MutableStateFlow(0)
    val itemCount: StateFlow<Int> = _itemCount.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val medicines: StateFlow<List<Medicine>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) repository.allMedicines
            else repository.searchMedicines(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.allMedicines.collect { list ->
                _itemCount.value = list.size
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _errorMessage.value = null
            val result = repository.refresh()
            result.onSuccess { count ->
                _lastUpdated.value = getCurrentTimestamp()
                saveLastUpdatedPref(_lastUpdated.value)
            }.onFailure { e ->
                _errorMessage.value = "Failed to refresh: ${e.message}"
            }
            _isRefreshing.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun getLastUpdatedPref(): String {
        val prefs = getApplication<Application>().getSharedPreferences("pharmacy_prefs", Context.MODE_PRIVATE)
        return prefs.getString("last_updated", "") ?: ""
    }

    private fun saveLastUpdatedPref(value: String) {
        val prefs = getApplication<Application>().getSharedPreferences("pharmacy_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("last_updated", value).apply()
    }

    private fun getCurrentTimestamp(): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}
