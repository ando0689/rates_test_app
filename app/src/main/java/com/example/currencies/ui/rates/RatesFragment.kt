package com.example.currencies.ui.rates

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.currencies.R
import com.example.currencies.di.Injectable
import com.example.currencies.repository.datamodels.Resource
import com.example.currencies.ui.common.Currency
import com.example.currencies.ui.common.ExchangeType
import com.example.currencies.ui.common.RateSortOrder
import kotlinx.android.synthetic.main.currencies_fragment.view.*
import javax.inject.Inject

class RatesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RatesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.currencies_fragment, container, false)

        view.refresh.setOnClickListener {
            viewModel.forceUpdateRates()
        }

        view.currency.setOnClickListener {
            viewModel.changeCurrency(Currency.RUR)
        }

        view.exchange.setOnClickListener {
            viewModel.changExchangeType(ExchangeType.NON_CASH)
        }

        view.sortDist.setOnClickListener {
            viewModel.sort(RateSortOrder.DISTANCE_ASC)
        }

        view.sortBuy.setOnClickListener {
            viewModel.sort(RateSortOrder.BUY_ASC)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RatesViewModel::class.java)
        viewModel.rateListItems.observe(this, Observer<Resource<List<RateListItem>>>{
            println(it)
            Log.d("test123", it.toString())
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
