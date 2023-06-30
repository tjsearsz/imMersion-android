package com.immersion.immersionandroid

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.AugmentedImageNode


class FragmentAR : Fragment(R.layout.fragment_a_r) {

    lateinit var sceneView: ArSceneView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)

        sceneView.addChild(
            AugmentedImageNode(
                sceneView.engine,
                imageName = "rabbit",
                bitmap = requireContext().assets.open("augmentedimages/qrcode.png")
                    .use(BitmapFactory::decodeStream)
            ).apply { loadModelGlbAsync(glbFileLocation = "models/spiderbot.glb") }
        )
    }
}