package com.immersion.immersionandroid.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.dataAccess.AugmentedImageRepository
import com.immersion.immersionandroid.databinding.FragmentARBinding
import com.immersion.immersionandroid.databinding.FragmentCompanyListBinding
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.immersionandroid.presentation.AugmentedRealityViewModel
// import com.immersion.immersionandroid.presentation.AugmentedRealityViewModel
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.properties.Delegates


class FragmentAR : Fragment(R.layout.fragment_a_r) {

    private lateinit var sceneView: ArSceneView

    private val imageNodeList: MutableMap<String, Node> = LinkedHashMap()

    private val viewModel: AugmentedRealityViewModel by activityViewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var currentLocation: Location

    private var _binding: FragmentARBinding? = null

    private var isBusinessOwner: Boolean by Delegates.notNull<Boolean>()

    private val binding get() = _binding!!

    private fun setupImageDatabase(result: List<AugmentedImage>) {

        sceneView.configureSession { session, config ->

            config.planeFindingMode = Config.PlaneFindingMode.DISABLED

            val database = AugmentedImageDatabase(session)

            // database.addImage()

            /*val hola = AugmentedImageNode("yes", null).apply {
                loadModelGlbAsync(glbFileLocation = it.modelURL)
            }*/

            Log.d("debugging", "paseando")

            Log.d("debugging", "el tamaño ${result.size}")

            if (result.isNotEmpty() && sceneView.children.isNotEmpty()) {
                Log.d("debugging", "A borrarlos!")

                imageNodeList.forEach { entry -> sceneView.removeChild(entry.value) }

                imageNodeList.clear()

            }

            result.forEach {
                val hola = UUID.randomUUID().toString().substring(0, 8)
                database.addImage(hola, it.bitmapImageURL)
                //database.addImage("whatever2", result[1].bitmapImageURL)

                val imageNode = AugmentedImageNode(hola, null).apply {
                    loadModelGlbAsync(glbFileLocation = it.modelURL)
                }

                imageNodeList[hola] = imageNode
                /*sceneView.addChild(AugmentedImageNode(hola, null).apply {
                    loadModelGlbAsync(glbFileLocation = it.modelURL)
                })*/

                sceneView.addChild(imageNode)
            }

            /*database.addImage("whatever", result[0].bitmapImageURL)
            database.addImage("whatever2", result[1].bitmapImageURL)*/

            config.augmentedImageDatabase = database

            session.configure(config)

            /*sceneView.onAugmentedImageUpdate += { augmentedImage ->
                Log.d("debugging", "llegue")
                Log.d("debugging", augmentedImage.name)
                Log.d("debugging", "${augmentedImage.trackingState}")
                Log.d("debugging", "${augmentedImage.anchors.size}")
                if (augmentedImage.isTracking && augmentedImage.anchors.isEmpty()) {
               //     if(modelNode?.)
                    Log.d("debugging", "${augmentedImage.name}")
                    Log.d("debugging", "los anchores${augmentedImage.anchors.size}")

                   val node2 = AugmentedImageNode(augmentedImage.name, null).apply {
                       //loadModelGlbAsync(glbFileLocation = result[1].modelURL)
                       loadModelGlbAsync(glbFileLocation = result[superlist.indexOfFirst { it == augmentedImage.name }].modelURL)
                   }

                    /************************ Normal node ************************/
                    val anchorImage = augmentedImage.createAnchor(augmentedImage.centerPose)
                    val node = ArModelNode()
                    node.apply {
                        //loadModelGlbAsync(glbFileLocation = if(augmentedImage.name == "whatever") result[1].modelURL else result[0].modelURL)
                        loadModelGlbAsync(glbFileLocation = result[superlist.indexOfFirst { it == augmentedImage.name }].modelURL)
                    }
                    node.anchor = anchorImage
                    /************************ Normal node ************************/

                    sceneView.addChild(node)
                    // sceneView.addChild(node2)
                }
            }*/

        }

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

        Log.d("debugging", "pedipermiso")


        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {

                currentLocation = location
                executeAugmentedReality()
            }
        }
    }

    private fun askForGPSPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun executeAugmentedReality() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = viewModel.getAugmentedRealitiesInOpenPositionsNearby(
                LatLng(
                    currentLocation.latitude,
                    currentLocation.longitude
                )
            )

            Log.d("debugging", "lo obtenido ${result.size}")

            lifecycleScope.launch(Dispatchers.Main) {
                Log.d("debugging", "bitmapeee")

                setupImageDatabase(result)

                Log.d("debugging", "mis hijos ${sceneView.children.size}")

                binding.icArgps.isEnabled = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentARBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isBusinessOwner =
            activity?.intent?.extras?.getBoolean("isBusinessOwner", false) ?: false

        binding.icUserAction.apply {
            text = if (isBusinessOwner) "Dashboard" else "Business Owner?"
            setOnClickListener {
                if (isBusinessOwner)
                    Intent(context, OwnershipActivity::class.java).also {
                        startActivity(it) //TODO: We need to apply one of those flags to avoid opening activity again and agian
                        requireActivity().finish()
                    }
                else
                    Intent(context, RegisterAsBusinessOwnerActivity::class.java).also {
                        startActivity(it)
                    }

            }
        }

        sceneView = view.findViewById(R.id.sceneView)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted)
                    getCurrentLocationOfUser()
                else {
                    Toast.makeText(
                        context,
                        "Permission denied for location. Cannot get available jobs",
                        Toast.LENGTH_SHORT
                    ).show()

                    requireActivity().finish()
                }
            }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.icArgps.setOnClickListener {
            binding.icArgps.isEnabled = false
            executeAugmentedReality()

        }


        // var hola = ModelNode(sceneView.engine).loadModelGlbAsync()

        /*sceneView.coroutineScope?.launch {
            val result =  ApolloAugmentedImageClient().getAugmentedImages()

            val modelNode = AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                .use(BitmapFactory::decodeStream)).loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW", onLoaded = {Log.d("debugging", "cargado")})

            sceneView.addChild(modelNode)
        }*/

        lifecycleScope.launch {

            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                binding.icArgps.isEnabled = false

                Log.d("debugging", "antes")
                getCurrentLocationOfUser()

                //if (currentLocation !== null) {
                /*  lifecycleScope.launch(Dispatchers.IO) {
                      //   var bitmap = getBitmap(result.imageURL)
                      //val result = AugmentedImageRepository().getAugmentedImages()


                      val result = viewModel.getAugmentedRealitiesInOpenPositionsNearby(
                          LatLng(
                              currentLocation.latitude,
                              currentLocation.longitude
                          )
                      )

                      lifecycleScope.launch(Dispatchers.Main) {
                          Log.d("debugging", "bitmapeee")

                          setupImageDatabase(result)
                          // sceneView.configureSession(::setupImageDatabase)

                          // result.forEach {
                          /* sceneView.onAugmentedImageUpdate +=
                               {augmentedImage ->

                                   Log.d("debugging", augmentedImage.name)
                               }*/

                          /* var it = result[0]
                              sceneView.addChild(

                                  AugmentedImageNode(
                                      // imageName = UUID.randomUUID().toString().substring(0, 8),
                                      imageName = superlist[0],
                                      //bitmap = it.bitmapImageURL,
                                      bitmap = null,
                                      onUpdate = {_, _-> Log.d("debugging", "actualice 1")}
                                  ).apply { loadModelGlbAsync(glbFileLocation = it.modelURL) }

                              )


                         /* sceneView.onAugmentedImageUpdate +=
                              {augmentedImage ->

                                  Log.d("debugging", augmentedImage.name)
                              }*/

                          it = result[1]

                          sceneView.addChild(

                              AugmentedImageNode(
                                  //imageName = UUID.randomUUID().toString().substring(0, 8),
                                  imageName = superlist[1],
                                  // bitmap = it.bitmapImageURL,
                                  bitmap = null,
                                  onUpdate = {_, _-> Log.d("debugging", "actualice 2")}
                              ).apply { loadModelGlbAsync(glbFileLocation = it.modelURL) }

                          )*/


                          // }

                          Log.d("debugging", "mis hijos ${sceneView.children.size}")

                      }
                  }*/
                // }
            }
        }

        /*sceneView.coroutineScope?.launchWhenCreated {

            val result =  async {ApolloAugmentedImageClient().getAugmentedImages()}

            result.await()



            sceneView.configureSession{arSession: ArSession, config: Config ->
                run {
                    database = AugmentedImageDatabase(arSession)
                    database.addImage("rabbit", requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                        .use(BitmapFactory::decodeStream))

                    AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = null).apply {
                        loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW")
                    }
                    config.augmentedImageDatabase = database

                    var hola = AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = null).apply {
                        loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW", onLoaded = {Log.d("debugging", "cargado")})
                    }

                    sceneView.addChild(hola)
                }
            }

                /*val modelNode = AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                    .use(BitmapFactory::decodeStream)).loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW", onLoaded = {Log.d("debugging", "cargado")})*/


            /*val hola = async {
                result.await()
                AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                .use(BitmapFactory::decodeStream)).loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW")
            }*/


            //sceneView.addChild(hola.await())


        }*/

        //var hola = Fragment.get
        /*lifecycleScope.launchWhenCreated {

            Log.d("debugging", "super arranco")
           val result =  ApolloAugmentedImageClient().getAugmentedImages()

            withContext(Dispatchers.Main){
                val modelNode = AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                    .use(BitmapFactory::decodeStream)).loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW", onLoaded = {Log.d("debugging", "cargado")})

                sceneView.addChild(modelNode)
            }

            Log.d("debugging", "arranque")

            // val hola = loadModel(this@FragmentAR.requireContext(), "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW")!!

           // val supermodel = ModelNode(sceneView.engine, modelInstance = hola)
            val modelNode = AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                .use(BitmapFactory::decodeStream)).loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW", onLoaded = {Log.d("debugging", "cargado")})

            Log.d("debugging", "pase")

            sceneView.addChild(modelNode)

           var children = sceneView.allChildren
            Log.d("debugging", " los children ${children.size}")
            Log.d("debugging", "el children ${children[1].isVisible}")

            Log.d("debugging", "el children ${(children[1] as AugmentedImageNode).imageName}")

            // Log.d("debugging", "el children ${children[1].}")



           // children[0].

        }*/

        /*lifecycleScope.launch {

            var job = withContext(Dispatchers.IO){
                Log.d("debugging", "voy a buscarlo")
                ApolloAugmentedImageClient().getAugmentedImages()
            }
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                Log.d("debugging", "lo eoncontre")
                Log.d("debugging", job.modelURL)
                sceneView.addChild(AugmentedImageNode(
                    sceneView.engine,
                    imageName = "rabbit",
                    bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                        .use(BitmapFactory::decodeStream)
                ).apply { loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW") })
            }
            //var resultado = ApolloAugmentedImageClient().getAugmentedImages()

            //val node = AugmentedImageNode(sceneView.engine, "testing", )

        }*/


        // with grpahql
        /*Thread{

            var resultado = ApolloAugmentedImageClient().getAugmentedImages()
            val imageUrl = "https://cdn.filestackcontent.com/Vi3fEpCOQ3Gv3JkBZUxb"
            val url = URL(imageUrl)
            Log.d("debugging", "casi casi")
            val bitmap = BitmapFactory.decodeStream(url.openStream())

            sceneView.addChild(
                AugmentedImageNode(
                    sceneView.engine,
                    imageName = "rabbit",
                    bitmap = bitmap
                ).apply { loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW") }
            )
        }.start()*/

        /*lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                modelNode.lo
            }
        }*/

        /*lifecycleScope.launch(Dispatchers.IO) {
            var resultado = ApolloAugmentedImageClient().getAugmentedImages()
            val imageUrl = "https://cdn.filestackcontent.com/Vi3fEpCOQ3Gv3JkBZUxb"
            val url = URL(imageUrl)
            Log.d("debugging", "casi casi")
            val bitmap = BitmapFactory.decodeStream(url.openStream())

            Log.d("debugging", resultado.modelURL)

            withContext(Dispatchers.Main){

                Log.d("debugging", bitmap.toString())
                sceneView.addChild(
                    AugmentedImageNode(
                        sceneView.engine,
                        imageName = "rabbit",
                        bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                            .use(BitmapFactory::decodeStream)
                    ).apply { loadModelGlbAsync(glbFileLocation = "models/spiderbot.glb") }
                )
            }


        }*/

        /*val viewModel: AugmentedRealityViewModel by viewModels()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect{
                    Log.d("tennis", "por aca llegue")
                    Log.d("tennis", it.augmentedImage.toString())

                    if(it.augmentedImage !== null) {
                        Log.d("tennis", it.augmentedImage.modelURL.toString())
                        sceneView.addChild(
                            AugmentedImageNode(
                                sceneView.engine,
                                imageName = "rabbit",
                                bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                                    .use(BitmapFactory::decodeStream)
                            ).apply { loadModelGlbAsync(glbFileLocation = it.augmentedImage.modelURL) }
                        )
                    }
                }
            }
        }*/

        /*lifecycleScope.launch {
            lifecycleScope.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    // Update UI elements
                    sceneView.addChild(
                        AugmentedImageNode(
                            sceneView.engine,
                            imageName = "rabbit",
                            bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                                .use(BitmapFactory::decodeStream)
                        ).apply { loadModelGlbAsync(glbFileLocation = it.augmentedImage!!.modelURL) }
                    )
                }
            }*/

        /*sceneView.addChild(
             AugmentedImageNode(
                 sceneView.engine,
                 imageName = "rabbit",
                 bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                     .use(BitmapFactory::decodeStream)
             ).apply { loadModelGlbAsync(glbFileLocation = "models/spiderbot.glb") }
         )*/


        /*GlobalScope.launch(Dispatchers.Main) {
            val bitmap = getBitmap()
            sceneView.addChild(
                AugmentedImageNode(
                    sceneView.engine,
                    imageName = "rabbit",
                    bitmap = bitmap
                ).apply { loadModelGlbAsync(glbFileLocation = "models/spiderbot.glb") }
            )
        }*/

        /* runBlocking {
             try {
                 Log.d("debugging", "voy a entrar!")
                 val bitmap = getBitmap()
                 Log.d("debugging", "aca llegue al menos")
                 sceneView.addChild(
                     AugmentedImageNode(
                         sceneView.engine,
                         imageName = "rabbit",
                         bitmap = bitmap
                     ).apply { loadModelGlbAsync(glbFileLocation = "models/spiderbot.glb") }
                 )
             }catch(err: Exception){
                 Log.e("debugging", err.toString())
             }
         }*/

        /*Thread{
            val imageUrl = "https://cdn.filestackcontent.com/Vi3fEpCOQ3Gv3JkBZUxb"
            val url = URL(imageUrl)
            Log.d("debugging", "casi casi")
            val bitmap = BitmapFactory.decodeStream(url.openStream())

            sceneView.addChild(
                AugmentedImageNode(
                    sceneView.engine,
                    imageName = "rabbit",
                    bitmap = bitmap
                ).apply { loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW") }
            )
        }.start()*/

        /*CoroutineScope(Dispatchers.IO).launch {
             val bitmap = getBitmap()

             Log.d("debugeando", bitmap.toString())
             withContext(Dispatchers.Main) {
                 Log.d("debugeando", "aca voy!!!!")
                 sceneView.addChild(
                     AugmentedImageNode(
                         sceneView.engine,
                         imageName = "rabbit",
                         bitmap = bitmap
                     ).apply { loadModelGlbAsync(glbFileLocation = "models/spiderbot.glb") }
                 )

                 Log.d("debugeando", sceneView.children.toString())
             }
         }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}