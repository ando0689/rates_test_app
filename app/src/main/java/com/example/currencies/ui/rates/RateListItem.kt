package com.example.currencies.ui.rates

import com.example.currencies.repository.datamodels.*
import com.example.currencies.ui.common.Currency
import com.example.currencies.ui.common.ExchangeType
import com.example.currencies.ui.common.RateSortOrder
import com.example.currencies.util.LocationManager

data class RateListItem(
    val bankId: String,
    val bankName: String,
    val bankLogoUrl: String,
    val nearestBranchId: String,
    val nearestBranchDistance: Float,
    val rateBuy: Double,
    val rateSell: Double
)


fun List<RateListItem>.sort(sortOrder: RateSortOrder): List<RateListItem>{
    return when(sortOrder){
        RateSortOrder.UNSORTED -> this
        RateSortOrder.DISTANCE_ASC -> sortedBy { it.nearestBranchDistance }
        RateSortOrder.DISTANCE_DESC -> sortedByDescending { it.nearestBranchDistance }
        RateSortOrder.BUY_ASC -> sortedBy { it.rateBuy }
        RateSortOrder.BUY_DESC -> sortedByDescending { it.rateBuy }
        RateSortOrder.SELL_ASC -> sortedBy { it.rateSell }
        RateSortOrder.SELL_DESC -> sortedByDescending { it.rateSell }
    }
}


fun Resource<List<Bank>>.toRateListItemsResource(currency: Currency, exchangeType: ExchangeType, sortOrder: RateSortOrder) =
    Resource(this.status, this.data?.toRateListItems(currency, exchangeType)?.sort(sortOrder), this.message)

private fun List<Bank>.toRateListItems(currency: Currency, exchangeType: ExchangeType) = mapNotNull {
        it.toRateListItem(currency, exchangeType)
    }

private fun Bank.toRateListItem(currency: Currency, exchangeType: ExchangeType): RateListItem?{
    val rate = getRateFor(currency, exchangeType) ?: return null
    val nearestBranch = getNearestBranch(LocationManager.getCurrentLocation())

    return RateListItem(
        bankId = id,
        bankName = name,
        bankLogoUrl = logoUrl,
        nearestBranchId = nearestBranch?.id ?: "",
        nearestBranchDistance = nearestBranch?.distanceFrom(LocationManager.getCurrentLocation()) ?: -1F,
        rateBuy = rate.buy,
        rateSell = rate.sell
    )
}
