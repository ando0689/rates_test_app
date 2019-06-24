package com.example.currencies.di

import com.example.currencies.ui.bankdetails.BankDetailsFragment
import com.example.currencies.ui.rates.RatesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCurrenciesFragment(): RatesFragment

    @ContributesAndroidInjector
    abstract fun contributeBankDetailsFragment(): BankDetailsFragment
}
