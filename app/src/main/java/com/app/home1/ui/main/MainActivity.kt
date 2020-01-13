package com.app.home1.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.app.home1.BR
import com.app.home1.R
import com.app.home1.base.activity.BaseActivity
import com.app.home1.base.binding.BaseAdapter
import com.app.home1.databinding.ActivityMainBinding
import com.app.home1.models.BaseCatBreed
import com.app.home1.models.CatBreed
import com.app.home1.ui.details.BreedDetailsActivity
import com.app.home1.ui.details.BreedDetailsActivity.Companion.PARAM_BREED_ID

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityVM>() {
    private val catBreedAdapter = BaseCatBreedAdapter()
    private val openCatBreedListener:BreedInfoOpenListener = object : BreedInfoOpenListener {
        override fun onOpenBreedInfo(breed: BaseCatBreed) {
            intent = Intent(this@MainActivity, BreedDetailsActivity::class.java).apply {
                putExtra(PARAM_BREED_ID, breed)
            }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
    }

    override fun onResume() {
        super.onResume()
        startObservingViewModel(viewModel)
    }

    override fun bindView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            setVariable(BR.viewModel, this@MainActivity.viewModel)
            lifecycleOwner = this@MainActivity
            catBreeds.adapter = catBreedAdapter
            catBreeds.apply {
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            }
        }
    }

    override fun createViewModel() = ViewModelProviders.of(this).get(MainActivityVM::class.java)

    override fun startObservingViewModel(viewModel: MainActivityVM) {
        super.startObservingViewModel(viewModel)
        addObservableLiveData(viewModel.getData())
        viewModel.getData().observe(this,
            Observer { list ->
                catBreedAdapter.setItems(list)
            })
    }

    private inner class BaseCatBreedAdapter: BaseAdapter(){
        init {
            addTypedItem (CatBreed::class.java, R.layout.item_base_cat_breed, BR.catBreed)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding?.setVariable(BR.breedOpenListener, openCatBreedListener)
        }

        fun setItems(newItems: List<BaseCatBreed>){
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    interface BreedInfoOpenListener {
        fun onOpenBreedInfo(breed: BaseCatBreed)
    }
}
