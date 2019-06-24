package com.example.currencies.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.currencies.api.ApiEmptyResponse
import com.example.currencies.api.ApiErrorResponse
import com.example.currencies.api.ApiResponse
import com.example.currencies.api.ApiSuccessResponse
import com.example.currencies.repository.datamodels.Resource

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")

        val catchSource = loadFromCatch()

        if (catchSource == null || this.shouldFetch(catchSource)) {
            fetchFromNetwork(catchSource)
        } else {
            setValue(Resource.success(catchSource))
        }
    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(catchData: ResultType?) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        setValue(Resource.loading(catchData))

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)

            when (response) {
                is ApiSuccessResponse -> {
                    saveCallResult(processResponse(response))
                    val catchSource = loadFromCatch()
                    result.value =
                        if(catchSource == null) Resource.error("Result was null", null)
                        else Resource.success(catchSource)
                }
                is ApiEmptyResponse -> {
                    val catchSource = loadFromCatch()
                    result.value =
                        if(catchSource == null) Resource.error("Result was null", null)
                        else Resource.success(catchSource)
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, catchData))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected abstract fun processResponse(response: ApiSuccessResponse<RequestType>): ResultType

    protected abstract fun saveCallResult(item: ResultType)

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromCatch(): ResultType?

    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}
