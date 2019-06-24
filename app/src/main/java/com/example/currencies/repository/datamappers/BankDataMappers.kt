package com.example.currencies.repository.datamappers

import android.location.Location
import com.example.currencies.api.responses.*
import com.example.currencies.repository.datamodels.*
import com.example.currencies.ui.common.Currency
import com.example.currencies.ui.common.ExchangeType
import com.example.currencies.ui.common.WeekDay

fun Map<String, BankResponse>.toBanks() = map {
        it.value.toBank(it.key)
    }.toList()


fun BankDetailsResponse.toBranches() = list.map {
        it.value.toBranch(it.key)
}.toList()


private fun BankResponse.toBank(id: String) = Bank(
    id = id,
    name = title,
    logoUrl = logo,
    branches = mutableListOf(),
    rates = list.toRates()
)

private fun Map<String, Map<String, RateResponse>>.toRates() = map {
        it.value.toRate(it.key)
    }.toList()

private fun Map<String, RateResponse>.toRate(key: String) = Rate(
    currency = Currency.values().find { it.key == key } ?: Currency.UNKNOWN,
    cash = get(ExchangeType.CASH.key)?.toRateValue(),
    nonCash = get(ExchangeType.NON_CASH.key)?.toRateValue()
)

private fun RateResponse.toRateValue() = RateValue(buy = buy, sell = sell)


private fun BranchResponse.toBranch(id: String) = Branch(
    id = id,
    name = title,
    address = address,
    contacts = contacts.toContacts(),
    workDays = workHours.map { it.toWorkDay() },
    location = Location("").apply {
        latitude = location.lat
        longitude = location.lng
    }
)


private fun WorkHourResponse.toWorkDay(): WorkDay {
        val fromDay = days.split("-").getOrNull(0) ?: days
        val toDay = days.split("-").getOrNull(1) ?: days
        val fromHour = hours.split("-").getOrNull(0) ?: hours
        val toHour = hours.split("-").getOrNull(1) ?: hours

        return WorkDay(
            fromDay = fromDay.toWeekDay() ?: WeekDay.MON, // TODO Well, not a good idea, but I am too tired now
            toDay = toDay.toWeekDay() ?: WeekDay.FRI, // TODO Well, not a good idea, but I am too tired now
            fromHour = fromHour,
            toHour = toHour
        )
}

// TODO it might throw exception - deal with it if have time
private fun String.toWeekDay() = WeekDay.values().find {
        this.toInt() == it.key
}

//TODO I am sure I have bugs here ))
private fun String.toContacts(): List<Contact>{
        val contacts = split(", ")
        val defaultCode = contacts.getOrNull(0)?.split(")")?.getOrNull(0)?.replace("(", "")

        return contacts.map {
            Contact(
                code = defaultCode ?: "",
                number = it.replace(defaultCode ?: "", "")
            )
        }.toList()
}