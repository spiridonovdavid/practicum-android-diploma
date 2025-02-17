package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.common.sharedprefs.interactor.SharedPrefsInteractor
import ru.practicum.android.diploma.common.sharedprefs.models.Filter

class FilterSettingsViewModel(private val sharedPrefsInteractor: SharedPrefsInteractor) : ViewModel() {

    private val _currentFilter = MutableLiveData<Filter>()
    val currentFilter: LiveData<Filter> get() = _currentFilter

    private val _updatedFilter = MutableLiveData<Filter>()
    val updatedFilter: LiveData<Filter> get() = _updatedFilter

    init {
        _currentFilter.value = sharedPrefsInteractor.getFilter()
    }

    fun updateFilter(filter: Filter) {
        sharedPrefsInteractor.updateFilter(filter)
        refreshUpdatedFilter()
    }

    fun clearFilterField(field: String) {
        sharedPrefsInteractor.clearFilterField(field)
        refreshUpdatedFilter()
    }

    fun clearFilter() {
        sharedPrefsInteractor.clearFilter()
        refreshUpdatedFilter()
    }

    fun refreshUpdatedFilter() {
        val updatedFilter = sharedPrefsInteractor.getFilter()
        _updatedFilter.value = updatedFilter
    }
    fun refreshCurrentFilter() {
        _currentFilter.value = sharedPrefsInteractor.getFilter()
    }
}
