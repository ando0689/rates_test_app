package com.example.currencies.ui.rates

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.currencies.repository.RatesRepository
import com.example.currencies.repository.datamodels.Resource
import com.example.currencies.ui.common.Currency
import com.example.currencies.ui.common.ExchangeType
import com.example.currencies.ui.common.RateSortOrder
import javax.inject.Inject

class RatesViewModel @Inject constructor(val ratesRepository: RatesRepository) : ViewModel() {

    private var currentSortOrder = RateSortOrder.UNSORTED
    private var currentCurrency = Currency.USD
    private var currentExchangeType = ExchangeType.CASH

    private val banks = ratesRepository.getBanksWithRates(true)

    val rateListItems = MediatorLiveData<Resource<List<RateListItem>>>()

    init {
        forceUpdateRates()
    }

    fun forceUpdateRates(){
        rateListItems.removeSource(banks)
        rateListItems.addSource(banks){
            if (it.data != null && it.data.isNotEmpty()){
                val source = ratesRepository.getBanksWithBranches(it.data)
                rateListItems.addSource(source){ resource ->
                    rateListItems.removeSource(source)
                    rateListItems.value = resource.toRateListItemsResource(currentCurrency, currentExchangeType, currentSortOrder)
                }
            } else {
                rateListItems.value = it.toRateListItemsResource(currentCurrency, currentExchangeType, currentSortOrder)
            }
        }
    }


    fun sort(order: RateSortOrder){
        if(order == currentSortOrder) return
        rateListItems.value?.let {
            val rates: List<RateListItem> = it.data ?: emptyList()
            rateListItems.value = it.copy(data = rates.sort(order))
        }.also {
            currentSortOrder = order
        }
    }

    fun changeCurrency(currency: Currency){
        if(currency == currentCurrency) return

        banks.value?.let {
            rateListItems.value = it.toRateListItemsResource(currency, currentExchangeType, currentSortOrder)
        }.also {
            currentCurrency = currency
        }
    }

    fun changExchangeType(exchangeType: ExchangeType){
        if(exchangeType == currentExchangeType) return

        banks.value?.let {
            rateListItems.value = it.toRateListItemsResource(currentCurrency, exchangeType, currentSortOrder)
        }.also {
            currentExchangeType = exchangeType
        }
    }
}

