package com.example.currencies.ui.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.R
import com.example.currencies.di.Injectable
import com.example.currencies.repository.datamodels.Resource
import com.example.currencies.repository.datamodels.Status
import com.example.currencies.ui.common.Currency
import com.example.currencies.ui.common.ExchangeType
import com.example.currencies.ui.common.RateSortOrder
import kotlinx.android.synthetic.main.rates_fragment.view.*
import javax.inject.Inject

class RatesFragment : Fragment(), Injectable, RatesListAdapter.ItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RatesViewModel

    private lateinit var adapter: RatesListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.rates_fragment, container, false)

        adapter = RatesListAdapter(this)
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.adapter = adapter

        view.refresh.setOnClickListener {
            viewModel.forceUpdateRates()
        }

        view.sortOrder.setOnClickListener {
            showSortDialog()
        }

        view.currency.setOnClickListener {
            showCurrenciesDialog()
        }

        view.exchangeType.setOnClickListener {
            showExchangeTypeDialog()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RatesViewModel::class.java)
        viewModel.rateListItems.observe(this, Observer<Resource<List<RateListItem>>> {
            when (it.status) {
                Status.LOADING -> Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
                Status.ERROR -> Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_LONG).show()
                Status.SUCCESS -> it.data?.let { items ->
                    adapter.updateItems(items)
                }
            }
        })
    }

    override fun onItemClicked(item: RateListItem) {
        findNavController().navigate(
            RatesFragmentDirections.showBankDetails(item.bankId, item.nearestBranchId)
        )
    }


    private fun showSortDialog() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Sort By")
            builder.setItems(R.array.sort_orders) { dialog, which ->
                val sortOrder = when (which) {
                    0 -> RateSortOrder.DISTANCE_ASC
                    1 -> RateSortOrder.DISTANCE_DESC
                    2 -> RateSortOrder.BUY_ASC
                    3 -> RateSortOrder.BUY_DESC
                    4 -> RateSortOrder.SELL_ASC
                    5 -> RateSortOrder.SELL_DESC
                    else -> RateSortOrder.UNSORTED
                }

                viewModel.sort(sortOrder)

                dialog.dismiss()
            }

            builder.create().show()
        }
    }


    private fun showCurrenciesDialog() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Choose Currency")
            builder.setItems(R.array.currencies) { dialog, which ->
                val currency = when (which) {
                    0 -> Currency.USD
                    1 -> Currency.EUR
                    2 -> Currency.RUR
                    3 -> Currency.AUD
                    4 -> Currency.CAD
                    5 -> Currency.CHF
                    6 -> Currency.GBP
                    7 -> Currency.GEL
                    8 -> Currency.JPY
                    9 -> Currency.XAU
                    else -> Currency.UNKNOWN
                }
                viewModel.changeCurrency(currency)

                dialog.dismiss()
            }

            builder.create().show()
        }
    }


    private fun showExchangeTypeDialog() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Choose Exchange Type")
            builder.setItems(R.array.exchange_type) { dialog, which ->
                val exchangeType = when (which) {
                    0 -> ExchangeType.CASH
                    1 -> ExchangeType.NON_CASH
                    else -> ExchangeType.CASH
                }

                viewModel.changExchangeType(exchangeType)

                dialog.dismiss()
            }

            builder.create().show()
        }
    }

}
