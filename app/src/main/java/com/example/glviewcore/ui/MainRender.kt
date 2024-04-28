package com.example.glviewcore.ui

import android.content.Context
import android.view.TextureView
import com.example.glviewcore.R
import com.example.glviewcore.glcore.math.Vec3
import com.example.glviewcore.glcore.model.object3d.Cube3D
import com.example.glviewcore.glcore.model.object3d.Plane3DHor
import com.example.glviewcore.glcore.model.object3d.Water3D
import com.example.glviewcore.glcore.model.utils.ColorARGB
import com.example.glviewcore.glcore.render.Render

class MainRender(private val context : Context, private  val textureView: TextureView) : Render(context, textureView) {

    private lateinit var mViewPort: IntArray

    private lateinit var centerCube: Cube3D

    ///------Init------///

    override fun initScene() {
        initBackground()
        initCamera()
        initTexture()

     //   drawBackground()
        drawCube()
        drawWater()

        setFrameLimit(120)
    }

    private fun initTexture() {
        textureManager.loadTextureIdRes(context, R.drawable.background, "background", 1f)
    }

    private fun initBackground() {
        mViewPort = intArrayOf(0, 0, textureView.width, textureView.height)
    }

    private fun initCamera() {
        camera.position = Vec3(0.5f,2f,2f)
        camera.lookAt = Vec3(0f,0f,0f)
        camera.updateViewMatrix()
    }

    private fun drawBackground() {
        for (i in -1..1) {
            for (j in -1..1) {
                val plane = Plane3DHor(0f, 0f, 0f, 1f, 1f)
                plane.translate = Vec3(i * 1f, 0f, j * 1f)
                plane.textureKey = "background"
                scene.addTextureObject(plane)
            }
        }
    }

    private fun drawCube() {
        val cubeSize = 0.4f
        val colorCube = ColorARGB(1f, 0.9f, 0.2f, 0.2f)

        centerCube = Cube3D(colorCube,cubeSize)
        centerCube.translate = Vec3(0f,cubeSize/2f,0f)
        scene.addPolygonObject(centerCube)
    }

    private fun drawWater() {
        val water = Water3D(0f,0.2f,0f,2.5f,2.5f)
        scene.addWaterObject(water)
    }


    ///------Render------///

    companion object {
        const val SPEED_ROTATE_ANIMATION = 120f
    }

    override fun render(deltaTime: Long) {
        updateRotationCube(deltaTime)
    }

    private fun updateRotationCube(deltaTime: Long) {
        val newRotYEnd: Float = centerCube.rotY + SPEED_ROTATE_ANIMATION * (deltaTime / 1000f)
        centerCube.setRotY(newRotYEnd)
    }

    ///

    override fun onStartRender() {

    }

    override fun onPostFrame() {

    }

    override fun onDestroyRender() {

    }
}