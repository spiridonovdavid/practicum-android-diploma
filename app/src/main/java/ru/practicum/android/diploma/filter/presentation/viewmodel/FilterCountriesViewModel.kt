package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.interactor.FilterInteractor
import ru.practicum.android.diploma.filter.domain.model.Country

class FilterCountriesViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var selectedCountry: Country? = null
    // private val state = MutableLiveData<CountryViewState>()
    // fun observeState(): LiveData<IndustryViewState> = state {

    // }
}
