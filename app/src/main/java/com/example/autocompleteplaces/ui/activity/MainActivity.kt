package com.example.autocompleteplaces.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autocompleteplaces.R
import com.example.autocompleteplaces.databinding.ActivityMainBinding
import com.example.autocompleteplaces.ui.adapter.SearchListAdapter
import com.example.autocompleteplaces.ui.viewmodel.MainActivityViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var placesClient: PlacesClient
    lateinit var adapter: SearchListAdapter
    lateinit var manager: RecyclerView.LayoutManager
    private var searchList: MutableList<AutocompletePrediction> = mutableListOf()
    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initControls()
    }

    private fun initControls() {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        val apiKey = "AIzaSyDFjbzwtDSTsbCKwwjcSakbWjmM6I6nIhw"
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        placesClient = Places.createClient(this)
        etInputtext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchResult(s.toString())
                viewModel.responseData.observe(this@MainActivity, Observer {
                    searchList.add(it)
                    adapter = SearchListAdapter(searchList, { position ->
                        Toast.makeText(
                            this@MainActivity,
                            searchList[position].getPrimaryText(null).toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        rvSearchList.visibility = View.GONE
                        etInputtext.text = null
                    })
                    manager = LinearLayoutManager(this@MainActivity)
                    rvSearchList.adapter = adapter
                    rvSearchList.layoutManager = manager
                })

                if (etInputtext.text.toString().length > 0) {
                    rvSearchList.visibility = View.VISIBLE
                } else {
                    searchList.clear()
                    adapter.notifyDataSetChanged()
                    rvSearchList.visibility = View.GONE
                }
            }

        })

    }


}


