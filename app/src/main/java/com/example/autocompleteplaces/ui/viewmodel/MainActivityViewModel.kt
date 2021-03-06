package com.example.autocompleteplaces.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainActivityViewModel"
    var responseData: MutableLiveData<AutocompletePrediction> = MutableLiveData()
    lateinit var placesClient: PlacesClient
    fun searchResult(queryText: String) {
        placesClient = Places.createClient(getApplication())
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setCountry("IN")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(queryText)
                .build()


        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                for (prediction in response.autocompletePredictions) {
                    responseData.value = prediction
                    Log.e(TAG, prediction.placeId)
                    Log.e(TAG, prediction.getPrimaryText(null).toString())
                    Log.e(TAG, prediction.getSecondaryText(null).toString())
                    Log.e(TAG, prediction.getFullText(null).toString())
                    Log.e(TAG, prediction.placeTypes.toString())
                }
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e(TAG, "Place not found: " + exception.statusCode)
                }
            }


    }

}