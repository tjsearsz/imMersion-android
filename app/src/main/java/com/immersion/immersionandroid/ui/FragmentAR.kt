package com.immersion.immersionandroid.ui

// import com.immersion.immersionandroid.presentation.AugmentedRealityViewModel
import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.sceneform.rendering.ViewRenderable
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.CustomDialogBinding
import com.immersion.immersionandroid.databinding.FragmentARBinding
import com.immersion.immersionandroid.domain.AugmentedImage
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.immersionandroid.domain.Job
import com.immersion.immersionandroid.presentation.AugmentedRealityViewModel
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.isTracking
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.collision.pickHitTest
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.Node
import io.github.sceneview.node.ViewNode
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

    private lateinit var summaryDialog: Dialog

    private var _summaryBinding: CustomDialogBinding? = null
    private val summaryBinding get() = _summaryBinding!!

    private fun setupImageDatabase(result: LinkedHashMap<AugmentedImage, List<IEmployerOwnerShip>> /*List<AugmentedImage>*/) {

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

            result.forEach { (augmentedImage, jobList) ->
                val hola = UUID.randomUUID().toString().substring(0, 8)
                database.addImage(hola, augmentedImage.bitmapImageURL)
                //database.addImage("whatever2", result[1].bitmapImageURL)
                Log.d("tapping", "el numero magico: $hola")
                val imageNode = AugmentedImageNode(hola, null, onUpdate = { node, image ->
                    Log.d("debugging", "la magia atras")
                    Log.d("debugging", "los super hijitos ${node.children.size}")
                    Log.d("debugging", "el nombre : ${node.imageName}")
                    Log.d("debugging", "el iamge : ${image.name}")
                    Log.d("debugging", "1 : ${augmentedImage.scale}")
                    Log.d("debugging", "2 : ${augmentedImage.summaryScale}")
                    Log.d("debugging", "3 : ${augmentedImage.summaryX}")
                    Log.d("debugging", "4 : ${augmentedImage.summaryZ}")
                    if (image.isTracking && node.children.isEmpty()) {
                        Log.d("debugging", "la magia compienza")
                        ViewRenderable.builder()
                            .setView(context, R.layout.ar_job_summary_layout)
                            .build()
                            .thenAccept { renderable: ViewRenderable ->
                                val viewNode = ViewNode()

                                val jobSize = jobList.size
                                Log.d("debugging", "el tañao ${jobSize}")
                                fillArJobDescription(renderable, jobList)

                                //val arJobTitle = renderable.view.findViewById<TextView>(R.id.arJobTitle)

                                // val hola = View.inflate(context, R.layout.testing_layout, null)

                                // arJobTitle.text = "mega test"


                                renderable.view.apply {

                                    alpha = 0.0f
                                    x = -3000.0f
                                    //translationX = -100.0f
                                }.animate().apply {
                                    duration = 3000
                                    alpha(1.0f)
                                    interpolator = AccelerateInterpolator()
                                    //translationX(4.0f)
                                    x(0.0f)
                                    start()

                                }

                                renderable.isShadowCaster = false
                                renderable.isShadowReceiver = false

                                /*val hola2 = renderable.view.animate().apply {
                                    alpha(1.0f)
                                    duration = 5000
                                    interpolator = AccelerateInterpolator()
                                }*/

                                /*renderable.view.findViewById<ImageButton>(R.id.buttonG)
                                    .setOnClickListener {
                                        Log.d("tapping", "presione el megaboton")
                                    }*/

                                viewNode.setRenderable(renderable)

                                viewNode.name = "looking"
                                // viewNode.position = Position(x = 1.0f, y = 0.0f, z = 0.0f) //TODO: THIS WORKS TOO BUT I PREFER THE TRANSFORM DOWN
                                //viewNode.rotation = Rotation(45.0f)
                                //viewNode.scale = Scale(1.0f)
                                viewNode.scale = Scale(augmentedImage.summaryScale)

                                viewNode.apply {
                                    onTap = { motionEvent, _ ->
                                        Log.d("tapping", "tapeando workaround")
                                        displayingSummaryInfo(jobList)
                                    }
                                    transform(
                                        position = Position(
                                            x = augmentedImage.summaryX,
                                            z = augmentedImage.summaryZ
                                        ), //Position(x = 0.7f, z= -0.3f), //Position(z = -4f),
                                        rotation = Rotation(x = 2.6f, y = 10f)
                                    )
                                }

                                /* val hola70: ViewPropertyAnimator?

                                 viewNode.renderableInstance.apply {
                                     hola70 = view?.apply {
                                             alpha = 0.0f
                                         }?.animate()
                                 }*/

                                /* val hola9 = viewNode.renderableInstance!!

                                 val non = hola9.animate(true).apply {
                                     setPropertyName("alpha")
                                     setFloatValues(0.0f, 1.0f)
                                     duration= 5000
                                     repeatCount = 10
                                 }*/

                                // viewNode.renderableInstance?.animate(false)?.start()

                                /*viewNode.renderableInstance?.animate(true)?.apply {
                                     setPropertyName("alpha")
                                     setFloatValues(0f, 1f)
                                     interpolator
                                     this.start()
                                 }?.start()*/

                                /*  viewNode.renderableInstance?.animate(false)?.apply {
                                      setPropertyName("alpha")
                                      setFloatValues(0f, 1f)
                                      repeatCount = 0
                                      duration = 5000
                                      start()
                                  }*/

                                /*val hola4 = viewNode.renderableInstance?.animate(true)

                                hola4?.apply {
                                    Log.d("tapping", "me llamaron")
                                    setPropertyName(
                                        "alpha"
                                    )
                                    duration = 3000
                                    setFloatValues(0f, 1f)
                                    interpolator = AccelerateInterpolator()
                                }?.start() */

                                /*viewNode.renderableInstance?.apply {
                                    objectanimat
                                }

                                val ani = ObjectAnimator.off*/
                                //hola2.start()
                                node.addChild(viewNode)

                                // non.start()
                            }
                    }
                }).apply {
                    loadModelGlbAsync(glbFileLocation = augmentedImage.modelURL, autoAnimate = true)
                    scale = Scale(augmentedImage.scale)
                    onTap = { motionEvent, renderable ->
                        Log.d("tapping", "imagen tapar, ${renderable}")
                        Log.d("tapping", "id del tappar, ${this.imageName}")

                    }
                    applyPosePosition =
                        true // this should fix the problem of not following the anchor thing, found on discord https://discord.com/channels/893787194295222292/895209031700979752/1129952261410390088
                }

                /*ViewRenderable.builder().setView(context, R.layout.testing_layout).build()
                    .thenAccept { renderable: ViewRenderable ->

                        Log.d("debugging", "then acceptaendo")
                        val hola = UUID.randomUUID().toString().substring(0, 8)
                        database.addImage(hola, it.bitmapImageURL)

                        val viewNode = ViewNode()
                        viewNode.setRenderable(renderable)
                        viewNode.position = Position(x = 0.0f, y = 0.0f, z = 0.0f)


                        val imageNode = AugmentedImageNode(hola, null).apply {
                            loadModelGlbAsync(glbFileLocation = it.modelURL)
                            addChild(viewNode)
                        }

                        imageNodeList[hola] = imageNode
                        sceneView.addChild(imageNode)
                    }*/

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

        /* sceneView.onTapAr = { hitresult, motionevent ->
             Log.d("tapping", "tapar ${hitresult}")
         }

         sceneView.onTap = { motionEvent, node, renderable ->
             Log.d("tapping", "normal tapar ${node?.name}, ${renderable}")
         }*/

        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return onSingleTap(e)
                }
            })

        sceneView.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
        }

    }

    private fun fillArJobDescription(
        renderable: ViewRenderable,
        jobList: List<IEmployerOwnerShip>
    ) {

        val jobSize = jobList.size

        for ((index, iEmployerOwnerShip) in jobList.withIndex()) {

            val job = iEmployerOwnerShip as Job
            if (jobSize == 1) {
                val displayJob =
                    renderable.view.findViewById<TextView>(R.id.displayJob2)

                displayJob.apply {
                    text = job.name
                    visibility = VISIBLE
                }
            } else if (jobSize == 2) {
                if (index == 0) {
                    val displayJob = renderable.view.findViewById<TextView>(R.id.displayJob1)
                    displayJob.apply {
                        text = job.name
                        visibility = VISIBLE
                    }
                } else {
                    val displayJob =
                        renderable.view.findViewById<TextView>(R.id.displayJob3).apply {
                            text = job.name
                            visibility = VISIBLE
                        }

                    displayJob.apply {
                        text = job.name
                        visibility = VISIBLE
                    }
                }
            } else {

                if (index < 3)
                    when (index) {
                        0 -> {
                            val displayJob =
                                renderable.view.findViewById<TextView>(R.id.displayJob1)

                            displayJob.apply {
                                text = job.name
                                visibility = VISIBLE
                            }
                        }

                        1 -> {
                            val displayJob =
                                renderable.view.findViewById<TextView>(R.id.displayJob2)
                            displayJob.apply {
                                text = job.name
                                visibility = VISIBLE
                            }

                        }

                        else -> {
                            val displayJob =
                                renderable.view.findViewById<TextView>(R.id.displayJob3)

                            displayJob.apply {
                                text = job.name
                                visibility = VISIBLE
                            }
                        }
                    }
                else {
                    val displayManyMore =
                        renderable.view.findViewById<TextView>(R.id.displayManyMore)
                    displayManyMore.visibility = VISIBLE

                    break
                }
            }
        }
    }

    private fun displayingSummaryInfo(jobList: List<IEmployerOwnerShip>) {

        summaryDialog = Dialog(requireContext())


        val params: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        params.apply {
            bottomMargin = 50
            topMargin = 50
        }

        val params2: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        params2.apply {
            bottomMargin = 50
        }
        // val layout = summaryBinding.dialogJobs
        // layout.removeAllViews()

        val layout = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null)
        val container = layout.findViewById<LinearLayout>(R.id.dialogJobs)

        container.removeAllViews()

        // val test = "<a href='http://www.google.com'> Google </a>";

        jobList.forEach {
            val job = it as Job

            val title = TextView(requireContext())
            title.apply {
                text = "Position: ${job.name} \n Available: ${job.positions}"
                gravity = Gravity.CENTER
            }

            val description = TextView(requireContext())
            description.apply {
               /* text =
                    " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras mollis suscipit semper. In hac habitasse platea dictumst. Aliquam viverra at nulla feugiat gravida. Quisque scelerisque tellus ex, a consequat sem ultricies quis. Nam euismod pretium ante, vel blandit nibh sollicitudin vitae. In sem mauris, fermentum a quam id, sollicitudin venenatis neque. Donec dignissim diam ligula, vitae consectetur nunc tristique a. Nullam feugiat purus nec risus accumsan tincidunt eu quis eros. Sed in neque quis mi convallis pharetra.\n" +
                            "\n" +
                            "Proin finibus diam eleifend, facilisis nibh at, aliquet risus. Donec eu malesuada est, eget dignissim orci. Morbi ultrices quis leo at semper. Mauris tempus auctor ligula, non imperdiet erat suscipit vel. Quisque eu leo mauris. Donec eget risus eget leo feugiat porttitor. Proin scelerisque sapien ex, vel lacinia lectus elementum sed. Cras aliquet euismod ex. Suspendisse varius libero vel ultrices tristique. Maecenas pellentesque et eros id sodales. Proin a pharetra ante. Duis dignissim ante arcu, in ultrices leo dictum vitae.\n" +
                            "\n" +
                            "Aenean sed vulputate nulla. Fusce sit amet iaculis turpis. Duis rhoncus lacinia lacus id feugiat. Quisque vitae consequat ex. Quisque ultricies dolor et ipsum tristique varius. Ut non imperdiet odio. Cras lobortis mattis placerat. Morbi rhoncus quam vel lacus molestie semper. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nullam vehicula varius mattis. Phasellus hendrerit porttitor diam, id interdum ipsum ultricies vitae. Etiam vel facilisis nibh, vitae porttitor odio.\n" +
                            "\n" +
                            "Sed dapibus ultricies luctus. In imperdiet elit pharetra lectus bibendum dapibus. Nulla vel erat blandit orci rhoncus facilisis id id enim. Aenean cursus enim dictum, porta ante sit amet, blandit ligula. Praesent porta feugiat purus, sed consectetur eros tincidunt in. Aliquam laoreet blandit justo, ut mattis ipsum bibendum vel. Maecenas pellentesque, justo sed dictum congue, magna massa viverra tortor, sit amet venenatis dui turpis id nisi. Donec mattis velit in arcu rhoncus, sit amet commodo purus pretium. Nulla at nulla vel risus convallis cursus. Vestibulum egestas velit id scelerisque porta. Nullam eget convallis eros, vel suscipit leo. Duis cursus, dolor sagittis pharetra ultrices, nunc erat porta arcu, ut porta ante purus ut risus.\n" +
                            "\n" +
                            "Vivamus tristique auctor purus, in aliquam enim interdum nec. Sed iaculis id felis eu imperdiet. Nunc sit amet nisl a ante auctor egestas. Etiam vel nisi at arcu cursus mollis. Mauris sollicitudin neque eget eleifend vestibulum. Donec eu elit velit. Vestibulum vitae orci accumsan, auctor quam in, imperdiet augue. Ut risus velit, placerat in maximus in, pellentesque eu dui. In hac habitasse platea dictumst. " */
                text = job.description
                gravity = Gravity.CENTER
                layoutParams = params
                // movementMethod = LinkMovementMethod.getInstance()
            }

            container.addView(title)
            container.addView(description)

            // if(test.isNotEmpty()){
            if (job.redirectURL !== null) {
                val link = TextView(requireContext())

                link.apply {
                    text = Html.fromHtml(
                        "<a href='${job.redirectURL}'> Click here </a>",
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
                    gravity = Gravity.CENTER
                    layoutParams = params2
                    movementMethod = LinkMovementMethod.getInstance()
                }

                link.setOnClickListener {
                    summaryDialog.dismiss()
                }

                container.addView(link)
            }
        }

        // this.summaryDialog.setContentView(layout.rootView)
        this.summaryDialog.setContentView(layout)
        this.summaryDialog.show()
    }

    private fun onSingleTap(motionEvent: MotionEvent): Boolean {
        val pickNode = sceneView.pickHitTest(motionEvent, false).node
        if (pickNode != null && pickNode is ViewNode) {
            pickNode.onTap(motionEvent, null)
            return true
        }

        return false
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
        _summaryBinding = CustomDialogBinding.inflate(layoutInflater, null, false)
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