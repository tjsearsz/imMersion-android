package com.immersion.immersionandroid.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.FragmentBranchMapsBinding
import com.immersion.immersionandroid.presentation.BranchViewModel

class BranchMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentBranchMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var marker: Marker
    private lateinit var autoCompleteFragment: AutocompleteSupportFragment
    private val viewModel: BranchViewModel by activityViewModels<BranchViewModel>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val PERMISSION_CODE = 101
    private val DEFAULT_ZOOM = 15f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBranchMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST) {
            Log.d("imMersion", "onMapsSdkInitialized: ${it.name}");
        }

        setUpMapsLogic()

        binding.floatingActionButton.setOnClickListener {

            val action =
                BranchMapsFragmentDirections.actionBranchInformationFragmentToBranchImageFragment2()
            view.findNavController().navigate(action)
        }
    }

    private fun setUpMapsLogic() {
        Places.initialize(
            requireContext(),
            "AIzaSyD3XsmM7yZStRJQ3e8X_D7tnctLwh6uxAI"
        ) //TODO: MOVE THIS BETTER PLACE
        autoCompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autoCompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                TODO("Not yet implemented")
            }

            override fun onPlaceSelected(place: Place) {
                // val add = place.address
                val latLng = place.latLng
                moveCamera(latLng, "Location")
            }

        })

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.icGps.setOnClickListener {
            getCurrentLocationOfUser()
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->

            if(isGranted){
                getCurrentLocationOfUser()
            }
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /* val mapFragment = supportFragmentManager
             .findFragmentById(R.id.map) as SupportMapFragment
         mapFragment.getMapAsync(this)*/

        getCurrentLocationOfUser()
    }

   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            PERMISSION_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCurrentLocationOfUser()
            }
        }
    }*/

    private fun askForGPSPermission(){
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun getCurrentLocationOfUser() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           /* ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_CODE
            )*/

            askForGPSPermission()

            return
        }

        val getLocation =
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

                if (location != null) {

                    currentLocation = location

                    val mapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

                    mapFragment.getMapAsync(this)
                }
            }
    }

    /* private fun actionSearchInit() {
         binding.inputSearch.setOnEditorActionListener { v, actionId, event ->

             if (actionId == EditorInfo.IME_ACTION_SEARCH
                 || actionId == EditorInfo.IME_ACTION_DONE
                 || event.action == KeyEvent.ACTION_DOWN
                 || event.action == KeyEvent.KEYCODE_ENTER
             ) {


                 geoLocate()
             }

             false
         }
     }

     private fun geoLocate() {
         val searchText = binding.inputSearch.text.toString()

         val geoCoder = Geocoder(this)

         Log.d("TESTING", searchText)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             geoCoder.getFromLocationName(
                 searchText, 1
             ) { addresses ->

                 if (addresses.size > 0) {
                     val address = addresses.get(0)
                     val latLong = LatLng(address.latitude, address.longitude)
                     Log.d("TESTING", address.toString())
                     runOnUiThread {
                         moveCamera(latLong, "Current Location")
                     }
                 }
             }
         } else {
             val addressList = geoCoder.getFromLocationName(searchText, 1)
             Log.d("TESTING", "AQUI LLEGUE")
             if (!addressList.isNullOrEmpty()) {
                 val address = addressList.get(0)
                 val latLong = LatLng(address.latitude, address.longitude)
                 moveCamera(latLong, "Current Location")
             }
         }


     }*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false


        // Add a marker in Sydney and move the camera
        /* val sydney = LatLng(-34.0, 151.0)
         mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)) */

        val pointCurrentLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
        // val markerOptions = MarkerOptions().position(pointCurrentLocation).title("Current Location")

        // mMap.addMarker(markerOptions)
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointCurrentLocation, DEFAULT_ZOOM))

        moveCamera(pointCurrentLocation, "My Location")

        // actionSearchInit()
    }

    private fun moveCamera(latLong: LatLng, title: String, zoom: Float = DEFAULT_ZOOM) {

        if (::marker.isInitialized)
            marker.remove()

        //Moving Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoom))

        //Placing marker
        val markerOptions = MarkerOptions().position(latLong).title(title)
        marker = mMap.addMarker(markerOptions)!!

        viewModel.changeBranchCoordinates(latLong)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}