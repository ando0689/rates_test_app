package com.example.currencies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencies.ui.bankdetails.BankDetailsViewModel
import com.example.currencies.ui.rates.RatesViewModel
import com.example.currencies.ui.common.ViewModelFactory


import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    abstract fun bindCurrenciesViewModel(ratesViewModel: RatesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BankDetailsViewModel::class)
    abstract fun bindBankDetailsViewModel(bankDetailsViewModel: BankDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
