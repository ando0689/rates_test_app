package com.example.currencies.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.currencies.api.*
import com.example.currencies.api.responses.BankDetailsResponse
import com.example.currencies.api.responses.BankResponse
import com.example.currencies.repository.datamodels.Bank
import com.example.currencies.repository.datamodels.Branch
import com.example.currencies.repository.datamappers.toBanks
import com.example.currencies.repository.datamappers.toBranches
import com.example.currencies.repository.datamodels.Resource
import com.example.currencies.repository.datamodels.Status
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepository @Inject constructor(val service: RatesService){

    var banks = mutableListOf<Bank>()
    var branches = mutableMapOf<String, List<Branch>>()

    fun getBanksWithRates(forceUpdate: Boolean = false): LiveData<Resource<List<Bank>>> {
        log("RatesRepository -> getBanksWithRates")
        return object : NetworkBoundResource<List<Bank>, Map<String, BankResponse>>(){

            override fun processResponse(response: ApiSuccessResponse<Map<String, BankResponse>>): List<Bank> {
                log("RatesRepository -> getBanksWithRates processResponse")
                return response.body.toBanks()
            }

            override fun saveCallResult(item: List<Bank>) {
                log("RatesRepository -> getBanksWithRates saveCallResult")
                banks.clear()
                banks.addAll(item)
                banks.forEach {
                    it.branches.clear()
                    it.branches.addAll(branches[it.id] ?: emptyList())
                }
            }

            override fun shouldFetch(data: List<Bank>?): Boolean {
                log("RatesRepository -> getBanksWithRates shouldFetch ${data == null || data.isEmpty() || forceUpdate}")
               return data == null || data.isEmpty() || forceUpdate
            }

            override fun loadFromCatch(): List<Bank>? {
                log("RatesRepository -> getBanksWithRates loadFromCatch")
                return banks
            }

            override fun createCall(): LiveData<ApiResponse<Map<String, BankResponse>>> {
                log("RatesRepository -> getBanksWithRates createCall")
                return service.getRates()
            }

        }.asLiveData()
    }

    fun getBranchesForBank(bankId: String, forceUpdate: Boolean = false): LiveData<Resource<List<Branch>>>{
        log("RatesRepository -> getBranchesForBank + $bankId")

        return object : NetworkBoundResource<List<Branch>, BankDetailsResponse>(){

            override fun processResponse(response: ApiSuccessResponse<BankDetailsResponse>): List<Branch> {
                log("RatesRepository -> getBranchesForBank processResponse")
               return response.body.toBranches()
            }

            override fun saveCallResult(item: List<Branch>) {
                log("RatesRepository -> getBranchesForBank saveCallResult")
                branches[bankId] = item
            }

            override fun shouldFetch(data: List<Branch>?): Boolean {
                log("RatesRepository -> getBranchesForBank shouldFetch ${data == null || data.isEmpty() || forceUpdate}")
                return data == null || data.isEmpty() || forceUpdate
            }

            override fun loadFromCatch(): List<Branch>? {
                log("RatesRepository -> getBranchesForBank loadFromCatch")
                return branches[bankId]
            }

            override fun createCall(): LiveData<ApiResponse<BankDetailsResponse>> {
                log("RatesRepository -> getBranchesForBank createCall")
                return service.getBankDetails(bankId)
            }

        }.asLiveData()
    }


    fun getBanksWithBranches(banks: List<Bank>): LiveData<Resource<List<Bank>>> {
        log("RatesRepository -> getRateListItems")
        return MediatorLiveData<Resource<List<Bank>>>().apply {
            val liveDatas = mutableSetOf<LiveData<Resource<List<Branch>>>>()

            fun updateWithBranches() {
                if (liveDatas.isEmpty()) {
                    log("RatesRepository -> getRateListItems updateWithBranches 111111111111")
                    this.value = Resource.success(banks)
                }
            }

            banks.forEach { bank ->
                val liveData = getBranchesForBank(bank.id)
                liveDatas.add(liveData)

                addSource(liveData) {
                    if(it.status == Status.SUCCESS){
                        log("RatesRepository -> getRateListItems Status.SUCCESS 111111111111")
                        bank.branches.clear()
                        bank.branches.addAll(it.data ?: emptyList())
                        liveDatas.remove(liveData)
                        removeSource(liveData)
                        updateWithBranches()
                    } else if(it.status == Status.ERROR){
                        log("RatesRepository -> getRateListItems Status.ERROR 11111111111")
                        liveDatas.remove(liveData)
                        removeSource(liveData)
                        updateWithBranches()
                    }
                }
            }
        }
    }


    private fun log(msg: String){
        Log.d("test1", msg)
    }

}