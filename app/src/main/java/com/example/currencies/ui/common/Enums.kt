package com.example.currencies.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.currencies.R

enum class Currency(val key: String, @StringRes val nameRes: Int, @DrawableRes val iconRes: Int) {
    USD("USD", R.string.currency_usd, R.drawable.ic_usd),
    EUR("EUR", R.string.currency_eur, R.drawable.ic_eur),
    RUR("RUR", R.string.currency_rur, R.drawable.ic_rur),
    AUD("AUD", R.string.currency_aud, R.drawable.ic_aud),
    CAD("CAD", R.string.currency_cad, R.drawable.ic_cad),
    CHF("CHF", R.string.currency_chf, R.drawable.ic_chf),
    GBP("GBP", R.string.currency_gbp, R.drawable.ic_gbp),
    GEL("GEL", R.string.currency_gel, R.drawable.ic_gel),
    JPY("JPY", R.string.currency_jpy, R.drawable.ic_jpy),
    XAU("XAU", R.string.currency_xau, R.drawable.ic_xau),
    UNKNOWN("UNK", R.string.currency_unknown, R.drawable.ic_unknown)
}

enum class ExchangeType(val key: String, @StringRes val nameRes: Int) {
    CASH("1", R.string.exchange_type_cash),
    NON_CASH("0", R.string.exchange_type_non_cash)
}

enum class WeekDay(val key: Int, @StringRes val nameRes: Int) {
    MON(1, R.string.day_mon),
    TUE(2, R.string.day_tue),
    WED(3, R.string.day_wed),
    THU(4, R.string.day_thu),
    FRI(5, R.string.day_fri),
    SAT(6, R.string.day_sat),
    SUN(7, R.string.day_sun),
}

enum class RateSortOrder{
    DISTANCE_ASC,
    DISTANCE_DESC,
    BUY_ASC,
    BUY_DESC,
    SELL_ASC,
    SELL_DESC,
    UNSORTED
}