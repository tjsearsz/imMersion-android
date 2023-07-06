package com.immersion.immersionandroid.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.dataAccess.ApolloAugmentedImageClient
import com.immersion.immersionandroid.presentation.AugmentedRealityViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.SceneView
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.BaseArFragment
import io.github.sceneview.ar.arcore.ArSession
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.model.GLBLoader.loadModel
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


// @AndroidEntryPoint
class FragmentAR : Fragment(R.layout.fragment_a_r) {

    lateinit var sceneView: ArSceneView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)


        // var hola = ModelNode(sceneView.engine).loadModelGlbAsync()

        /*sceneView.coroutineScope?.launch {
            val result =  ApolloAugmentedImageClient().getAugmentedImages()

            val modelNode = AugmentedImageNode(sceneView.engine, imageName = "rabbit", bitmap = requireContext().assets.open("augmentedimages/Hoobastank_album.png")
                .use(BitmapFactory::decodeStream)).loadModelGlbAsync(glbFileLocation = "https://cdn.filestackcontent.com/vZvdHVwZR2mCLKPXuBsW", onLoaded = {Log.d("debugging", "cargado")})

            sceneView.addChild(modelNode)
        }*/

        lifecycleScope.launch {

            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                val result = ApolloAugmentedImageClient().getAugmentedImages()

                        // result.await()

                lifecycleScope.launch(Dispatchers.IO){
                    var bitmap = getBitmap(result.imageURL)

                    lifecycleScope.launch(Dispatchers.Main){
                        Log.d("debugging", "bitmapeee")

                        sceneView.addChild(
                            AugmentedImageNode(
                                // sceneView.engine,
                                imageName = "rabbit",
                                bitmap = bitmap
                            ).loadModelGlbAsync(glbFileLocation = result.modelURL))
                    }
                }



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

    private fun getBitmap(URL: String): Bitmap {
        // val imageUrl = "https://cdn.filestackcontent.com/Vi3fEpCOQ3Gv3JkBZUxb"
        //val url = URL(imageUrl)
        val url = URL(URL)
        Log.d("debugging", "casi casi")
        return BitmapFactory.decodeStream(url.openStream())
    }
}