package com.example.project2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaceFragment : Fragment() {

    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var placeAdapter: PlaceAdapter
    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private val places = listOf("The Factory", "Western Fair Sports Centre", "Nova Era Bakery", "London Muay Thai", "Trillium Glass","LA Mood Comics & Games","London Fire Station")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_place, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPlaces)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val placeAdapter = PlaceAdapter(places)
        recyclerView.adapter = placeAdapter

        return view
    }

    private fun fetchNearbyPlaces() {
        val queries = listOf("establishment", "college", "cafe", "restaurant", "park", "bank", "hospital", "grocery", "gym")

        val placesList = mutableListOf<Place>()
        for (query in queries) {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setLocationRestriction(
                    RectangularBounds.newInstance(
                    LatLng(latitude - 0.1, longitude - 0.1),
                    LatLng(latitude + 0.1, longitude + 0.1)
                ))
                .build()

            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions

                val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

                for (prediction in predictions) {
                    val placeId = prediction.placeId
                    val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build()

                    placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { fetchPlaceResponse ->
                        val place = fetchPlaceResponse.place
                        // Add the place to the list if it's not already present
                        if (!placesList.contains(place)) {
                            placesList.add(place)
                        }

                        println("PLaces"+ placesList)

                    }.addOnFailureListener { exception ->
                        Log.e("Error", "fetchNearbyPlaces: " +exception.message )
                        // Handle the failure to fetch place details
                    }
                }
            }.addOnFailureListener { exception ->
                // Handle the error if the search fails
                Log.e("Error123",""+exception.message)
            }
        }
    }

}

