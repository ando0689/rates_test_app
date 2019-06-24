package com.example.currencies.ui.bankdetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.currencies.R
import com.example.currencies.di.Injectable
import com.example.currencies.repository.datamodels.Bank
import com.example.currencies.repository.datamodels.Resource
import com.example.currencies.repository.datamodels.Status
import com.example.currencies.repository.datamodels.distanceFrom
import com.example.currencies.util.LocationManager
import kotlinx.android.synthetic.main.bank_details_fragment.view.*
import javax.inject.Inject

class BankDetailsFragment : Fragment(), Injectable {

    lateinit var rootView: View
    lateinit var params: BankDetailsFragmentArgs

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BankDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.bank_details_fragment, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BankDetailsViewModel::class.java)

        viewModel.bank.observe(this, Observer<Resource<Bank>> {
            when (it.status) {
                Status.LOADING -> Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
                Status.ERROR -> Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_LONG).show()
                Status.SUCCESS -> it.data?.let { bank ->
                    initUi(bank)
                }
            }
        })

        arguments?.let {
            params = BankDetailsFragmentArgs.fromBundle(it)
            viewModel.bankWithId(params.bankId)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initUi(bank: Bank) {
        val branch = bank.branches.find { it.id == params.branchId }

        rootView.apply {
            bankName.text = "Bank name: ${bank.name}"
            branchName.text = "Branch Name: ${branch?.name?.get("en")}"
            address.text = "Address: ${branch?.address?.get("en")}"
            contactNumbers.text = "Contacts: ${branch?.contacts}"
            workDays.text = "Work Days:\n\n${branch?.workDays?.joinToString("\n-------\n")}"
            rates.text = "Rates:\n\n${bank.rates.joinToString("\n-------\n")}"
            branches.text =
                "Branches:\n\n${bank.branches.map { it.name["en"] to it.distanceFrom(LocationManager.getCurrentLocation()) }.joinToString(
                    "\n-------\n"
                )}"
        }
    }

}
