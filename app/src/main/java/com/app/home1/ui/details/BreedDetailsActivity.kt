package com.app.home1.ui.details

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.home1.BR
import com.app.home1.R
import com.app.home1.base.activity.BaseActivity
import com.app.home1.databinding.ActivityBreedDetailsBinding
import com.app.home1.models.CatBreed
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.activity_breed_details.*


class BreedDetailsActivity : BaseActivity<ActivityBreedDetailsBinding, BreedDetailsActivityVM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
    }

    override fun bindView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_breed_details)
        binding.apply {
            setVariable(BR.viewModel, this@BreedDetailsActivity.viewModel)
            lifecycleOwner = this@BreedDetailsActivity
//            catBreeds.adapter = catBreedAdapter
//            catBreeds.apply {
//                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
//            }
        }
    }

    override fun createViewModel() = ViewModelProviders.of(this).get(BreedDetailsActivityVM::class.java)

    override fun startObservingViewModel(viewModel: BreedDetailsActivityVM) {
        super.startObservingViewModel(viewModel)


        intent.getSerializableExtra(PARAM_BREED_ID)?.also{viewModel.setBreed(it as CatBreed)}
        addObservableLiveData(viewModel.getData())
        viewModel.getData().observe(this,
            Observer { breedImageInfo ->
                binding.breedImage.setImageURI(breedImageInfo.url)
            })
    }

    companion object {
        const val PARAM_BREED_ID = "BREED_ID"
    }
}
