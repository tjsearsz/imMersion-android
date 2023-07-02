package com.immersion.immersionandroid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.AugmentedImageNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.System.err
import java.net.URL


class FragmentAR : Fragment(R.layout.fragment_a_r) {

    lateinit var sceneView: ArSceneView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)

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

        Thread{
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
        }.start()

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

    private suspend fun getBitmap(): Bitmap? {
        val imageUrl = "https://cdn.filestackcontent.com/Vi3fEpCOQ3Gv3JkBZUxb"
        val url = URL(imageUrl)
        Log.d("debugging", "casi casi")
        return BitmapFactory.decodeStream(url.openStream())
    }
}