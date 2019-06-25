package com.example.currencies.repository.datamodels

import android.location.Location
import com.example.currencies.ui.common.Currency
import com.example.currencies.ui.common.ExchangeType
import com.example.currencies.ui.common.WeekDay

data class Bank(
    val id: String,
    val name: String,
    val logoUrl: String,
    val branches: MutableList<Branch> = mutableListOf(),
    val rates: List<Rate>
)

data class Branch(
    val id: String,
    val name: Map<String, String>,
    val address: Map<String, String>,
    val location: Location,
    val contacts: String,
    val workDays: List<WorkDay>
)

data class Contact(
    val code: String,
    val number: String)

data class WorkDay(
    val fromDay: WeekDay,
    val toDay: WeekDay,
    val fromHour: String,
    val toHour: String
)

data class Rate(
    val currency: Currency,
    val cash: RateValue?,
    val nonCash: RateValue?
)

data class RateValue(
    val buy: Double,
    val sell: Double
)


////////////////////////////////// Extensions

fun Branch.distanceFrom(location: Location) = location.distanceTo(this.location)

fun Bank.sortBranchesByDistance(location: Location): List<Branch>{
    branches.sortBy {
        it.distanceFrom(location)
    }

    return branches
}

fun Bank.getNearestBranch(location: Location) = sortBranchesByDistance(location).getOrNull(0)

fun Bank.getRateFor(currency: Currency, exchangeType: ExchangeType): RateValue?{
    val rateValue = rates.find {
        it.currency == currency
    } ?: return null

    return if(ExchangeType.CASH == exchangeType) rateValue.cash else rateValue.nonCash
}
