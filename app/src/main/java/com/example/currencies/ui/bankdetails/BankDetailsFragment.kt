package com.example.currencies.ui.bankdetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.currencies.R

class BankDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = BankDetailsFragment()
    }

    private lateinit var viewModel: BankDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bank_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BankDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
