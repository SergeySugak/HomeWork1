package com.app.home1.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.app.home1.base.dialogs.MessageDialogFragment
import com.app.home1.ui.main.MainActivity

abstract class BaseActivity<B: ViewDataBinding, VM: BaseVM<*>>: AppCompatActivity() {

    lateinit var binding: B
    open val viewModel: VM by lazy { createViewModel() }
    private val observableLiveData: HashSet<LiveData<*>> = HashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        startObservingViewModel(viewModel)
    }

    override fun onStop() {
        val owner = binding.lifecycleOwner
        if (owner != null) {
            observableLiveData.forEach{
                it.removeObservers(owner)
            }
        }
        super.onStop()
    }

    fun addObservableLiveData(observed: LiveData<*>){
        observableLiveData.add(observed)
    }

    abstract fun bindView()

    abstract fun createViewModel(): VM

    open fun startObservingViewModel(viewModel: VM){
        addObservableLiveData(viewModel.getError())
        viewModel.getError().observe(this,
            Observer { throwable ->
                throwable?.let{
                    if (!MessageDialogFragment.isShown(this, MSG_FGMNT_ERROR)) {
                        MessageDialogFragment.showError(
                            this,
                            it,
                            MessageDialogFragment.REQUEST_CODE_NONE,
                            null,
                            MSG_FGMNT_ERROR
                        )
                        viewModel.setErrorProcessed()
                    }
                }
            })
    }

    companion object {
        const val MSG_FGMNT_ERROR = "MSG_FGMNT_ERROR"
    }
}