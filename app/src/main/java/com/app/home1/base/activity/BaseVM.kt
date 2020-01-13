package com.app.home1.base.activity

import android.app.Application
import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job

abstract class BaseVM<T>(application: Application): AndroidViewModel(application) {
    //Показываем/скрываем progressbar при загрузке данных
    var inProgress = ObservableBoolean(false)

    //корневой job
    var parentJob = Job()

    val error = MutableLiveData<Throwable>()
    //список объектов, полученных с сервера
    val data = MutableLiveData<T>()

    fun getData(): LiveData<T>{
        return data
    }

    override fun onCleared() {
        if (parentJob.isActive) {
            parentJob.cancel()
        }
        super.onCleared()
    }

    fun getError(): LiveData<Throwable> {
        return error
    }

    fun setErrorProcessed(){
        error.value = null
    }

    abstract fun load()
}