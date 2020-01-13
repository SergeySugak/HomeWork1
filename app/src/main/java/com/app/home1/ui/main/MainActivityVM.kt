package com.app.home1.ui.main

import android.app.Application
import androidx.databinding.ObservableBoolean
import com.app.home1.R
import com.app.home1.base.activity.BaseVM
import com.app.home1.models.BaseCatBreed
import com.app.home1.models.CatBreed
import com.app.home1.network.api.CatsApi
import com.app.home1.network.retrofit.RetrofitClient
import com.app.home1.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

class MainActivityVM(application: Application) : BaseVM<List<CatBreed>>(application) {
    private lateinit var catsApi: CatsApi

    //Main loading function.
    //Loads data using coroutine on IO dispatcher.
    //Throws exception in any case when response code is not 200.
    override fun load(){
        val context: Application = getApplication()
        if (!Network.isOnline(context)) {
            error.value = Exception(context.getString(R.string.err_no_network))
            return
        }

        CoroutineScope(Dispatchers.IO + parentJob).launch{
            try {
                inProgress.set(true)
                if (!::catsApi.isInitialized) {
                    catsApi = RetrofitClient.getRetrofitService(CatsApi::class.java,
                        CatsApi.BaseURL, null)
                }
                val response = catsApi.listBreeds()
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    data.postValue(response.body())
                }
                else {
                    if (!response.isSuccessful) {
                        error.value = Exception(response.errorBody().toString())
                    } else {
                        error.value = Exception(response.body().toString())
                    }
                }
            }
            finally{
                inProgress.set(false)
            }
        }
    }

    init{
        load()
    }
}