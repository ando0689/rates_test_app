package com.example.currencies.ui.bankdetails

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel;
import com.example.currencies.repository.RatesRepository
import com.example.currencies.repository.datamodels.Bank
import com.example.currencies.repository.datamodels.Resource
import javax.inject.Inject

class BankDetailsViewModel @Inject constructor(ratesRepository: RatesRepository) : ViewModel() {

    private val banks = ratesRepository.getBanksWithRates(true)

    val bank = MediatorLiveData<Resource<Bank>>()

    fun bankWithId(bankId: String) {
        bank.removeSource(banks)
        bank.addSource(banks) { resource ->
            if (resource.data != null && resource.data.isNotEmpty()) {
                bank.value = Resource(resource.status, resource.data.find { it.id == bankId }, resource.message)
            } else {
                bank.value = Resource.error("No Bank", null)
            }
        }
    }

}
