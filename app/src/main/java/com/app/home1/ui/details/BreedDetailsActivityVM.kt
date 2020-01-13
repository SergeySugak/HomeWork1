package com.app.home1.ui.details

import android.app.Application
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.app.home1.R
import com.app.home1.base.activity.BaseVM
import com.app.home1.models.CatBreed
import com.app.home1.models.CatBreedImageInfo
import com.app.home1.network.api.CatsApi
import com.app.home1.network.retrofit.RetrofitClient
import com.app.home1.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

class BreedDetailsActivityVM(application: Application) : BaseVM<CatBreedImageInfo>(application) {
    private lateinit var catsApi: CatsApi
    val breed = ObservableField<CatBreed>()

    //Основная функиция загрузки данных по фото пород
    //Если порода равна null - очищаем данные (BaseVM.data)
    //Из полученного набора берет первую
    //(на самом деле в CatsApi так же прописано максимальное количество получаемых значений равное 1)
    //Загружает на IO диспетчере
    //Если ответ не равен HTTP_OK - бросаем исключение
    override fun load() {
        val tmpBreed = breed.get()
        if (tmpBreed == null){
            data.postValue(null)
            return
        }

        val context: Application = getApplication()
        if (!Network.isOnline(context)) {
            error.value = Exception(context.getString(R.string.err_no_network))
            return
        }

        if (!::catsApi.isInitialized) {
            catsApi = RetrofitClient.getRetrofitService(
                CatsApi::class.java,
                CatsApi.BaseURL, null)
        }

        CoroutineScope(Dispatchers.IO + parentJob).launch{
            inProgress.set(true)
            try {
                val response = catsApi.getBreedImageInfo(breedId = tmpBreed.id)
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    data.postValue(response.body()?.get(0))
                } else {
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

    //При переустановке породы выполняем перезапрос данных
    fun setBreed(value: CatBreed?){
        if (breed.get() != value) {
            breed.set(value)
            load()
        }
    }

}