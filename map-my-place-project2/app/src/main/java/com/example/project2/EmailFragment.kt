package com.example.project2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailFragment : Fragment() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var btnEmail: MaterialButton

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val DEFAULT_ZOOM = 16f
    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var currentLatLng: String
    private lateinit var address: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_email, container, false)
        etEmail = view.findViewById(R.id.etEmail)
        btnEmail = view.findViewById(R.id.btnEmail)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        enableMyLocation()

        btnEmail.setOnClickListener {
            sendEmail()
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        // Get the last known location from the FusedLocationProviderClient
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Get the latitude and longitude of the current location
                    var LatLng = LatLng(location.latitude, location.longitude)
                    currentLatLng = LatLng.toString()
                    address = getAddressFromLatLng(requireContext(),LatLng)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Unable to get current location.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address> =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                return address.getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "Unknown Location Name"
    }


    private fun sendEmail() {
        val email = etEmail.text.toString().trim()

        if (isValidEmail(email)) {
            val uriText = "mailto:$email" +
                    "?subject=" + "$currentLatLng" +
                    "&body=" + "address name: $address"
            val uri = Uri.parse(uriText)
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = uri
            startActivity(
                Intent.createChooser(mailIntent, "Send Email").addFlags(FLAG_ACTIVITY_NEW_TASK)
            )

        } else {
            Toast.makeText(
                requireContext(),
                "Please enter a valid email address.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

}